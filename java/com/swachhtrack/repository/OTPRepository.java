package com.swachhtrack.repository;

import com.swachhtrack.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    Optional<OTP> findByMobileNumberAndOtpCodeAndUsedFalseAndExpiryTimeAfter(
            String mobileNumber, String otpCode, LocalDateTime currentTime);

    Optional<OTP> findTopByMobileNumberOrderByCreatedAtDesc(String mobileNumber);

    void deleteByExpiryTimeBefore(LocalDateTime expiryTime);
}
