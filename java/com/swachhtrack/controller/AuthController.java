package com.swachhtrack.controller;

import com.swachhtrack.dto.*;
import com.swachhtrack.entity.User;
import com.swachhtrack.security.JwtUtil;
import com.swachhtrack.service.OTPService;
import com.swachhtrack.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OTPService otpService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(OTPService otpService, UserService userService, JwtUtil jwtUtil) {
        this.otpService = otpService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse<String>> requestOtp(@Valid @RequestBody AuthRequest request) {
        String otp = otpService.generateOtp(request.getMobileNumber());
        return ResponseEntity.ok(ApiResponse.success("OTP sent (simulated)", otp));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyOtp(@Valid @RequestBody OTPVerificationRequest request) {
        boolean ok = otpService.verifyOtp(request.getMobileNumber(), request.getOtpCode());
        if (!ok) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid or expired OTP"));
        }
        User user = userService.getOrCreateUser(request.getMobileNumber());
        String token = jwtUtil.generateToken(user.getMobileNumber(), user.getId());
        return ResponseEntity.ok(ApiResponse.success(
                "Login success",
                new AuthResponse(token, user.getId(), user.getMobileNumber(), "USER")));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<ApiResponse<Void>> validateToken() {
        return ResponseEntity.ok(ApiResponse.success("Token is valid", null));
    }
}
