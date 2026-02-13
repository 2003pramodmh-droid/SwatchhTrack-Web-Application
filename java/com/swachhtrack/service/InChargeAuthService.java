package com.swachhtrack.service;

import com.swachhtrack.entity.InCharge;
import com.swachhtrack.repository.InChargeRepository;
import com.swachhtrack.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class InChargeAuthService {

    private final InChargeRepository inChargeRepository;
    private final OTPService otpService;
    private final JwtUtil jwtUtil;

    public InChargeAuthService(InChargeRepository inChargeRepository, OTPService otpService, JwtUtil jwtUtil) {
        this.inChargeRepository = inChargeRepository;
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String requestOtp(String mobileNumber) {
        Optional<InCharge> inChargeOptional = inChargeRepository.findByMobileNumber(mobileNumber);
        if (inChargeOptional.isEmpty()) {
            throw new RuntimeException("In-Charge not found with this mobile number");
        }

        InCharge inCharge = inChargeOptional.get();
        if (!Boolean.TRUE.equals(inCharge.getActive())) {
            throw new RuntimeException("In-Charge account is inactive");
        }

        return otpService.generateOtp(mobileNumber);
    }

    @Transactional
    public String verifyOtpAndLogin(String mobileNumber, String otpCode) {
        boolean isValid = otpService.verifyOtp(mobileNumber, otpCode);
        if (!isValid) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        InCharge inCharge = inChargeRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("In-Charge not found"));

        inCharge.setVerified(true);
        inChargeRepository.save(inCharge);

        String token = jwtUtil.generateTokenWithRole(mobileNumber, "INCHARGE");
        log.info("In-Charge logged in successfully: {}", mobileNumber);
        return token;
    }

    public InCharge getInChargeByMobileNumber(String mobileNumber) {
        return inChargeRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("In-Charge not found"));
    }
}
