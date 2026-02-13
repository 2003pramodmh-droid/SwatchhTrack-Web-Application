package com.swachhtrack.service;

import com.swachhtrack.entity.OTP;
import com.swachhtrack.repository.OTPRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class OTPService {

    private final OTPRepository otpRepository;
    private final Random random = new Random();

    @Value("${otp.expiration.minutes}")
    private int otpExpiryMinutes;

    @Value("${otp.max.retry.attempts}")
    private int maxRetries;

    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public String generateOtp(String mobileNumber) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        OTP entity = OTP.builder()
                .mobileNumber(mobileNumber)
                .otpCode(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
                .used(false)
                .retryAttempts(0)
                .build();
        otpRepository.save(entity);
        log.info("OTP for {} is {} (simulated)", mobileNumber, otp);
        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String otpCode) {
        Optional<OTP> otpOpt = otpRepository.findTopByMobileNumberOrderByCreatedAtDesc(mobileNumber);
        if (otpOpt.isEmpty()) return false;
        OTP otp = otpOpt.get();

        if (otp.getUsed() || otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!otp.getOtpCode().equals(otpCode)) {
            int attempts = otp.getRetryAttempts() + 1;
            otp.setRetryAttempts(attempts);
            otpRepository.save(otp);
            return attempts < maxRetries;
        }

        otp.setUsed(true);
        otpRepository.save(otp);
        return true;
    }
}
