package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.AuthRequest;
import com.swachhtrack.dto.OTPVerificationRequest;
import com.swachhtrack.service.InChargeAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/incharge-auth")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class InChargeAuthController {

    private final InChargeAuthService inChargeAuthService;

    public InChargeAuthController(InChargeAuthService inChargeAuthService) {
        this.inChargeAuthService = inChargeAuthService;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<String>> requestOtp(@Valid @RequestBody AuthRequest request) {
        try {
            String message = inChargeAuthService.requestOtp(request.getMobileNumber());
            return ResponseEntity.ok(ApiResponse.success("OTP sent (simulated)", message));
        } catch (Exception e) {
            log.error("Error requesting OTP for in-charge", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to send OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Map<String, String>>> verifyOtp(
            @Valid @RequestBody OTPVerificationRequest request) {
        try {
            String token = inChargeAuthService.verifyOtpAndLogin(
                    request.getMobileNumber(),
                    request.getOtpCode());

            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            data.put("mobileNumber", request.getMobileNumber());
            data.put("role", "INCHARGE");

            return ResponseEntity.ok(ApiResponse.success("Login successful", data));
        } catch (Exception e) {
            log.error("Error verifying OTP for in-charge", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
