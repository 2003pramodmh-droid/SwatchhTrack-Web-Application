package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.ComplaintResponse;
import com.swachhtrack.dto.InChargeDashboardStats;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.service.InChargeAuthService;
import com.swachhtrack.service.InChargeDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incharge/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class InChargeDashboardController {

    private final InChargeDashboardService dashboardService;
    private final InChargeAuthService inChargeAuthService;

    public InChargeDashboardController(InChargeDashboardService dashboardService,
                                       InChargeAuthService inChargeAuthService) {
        this.dashboardService = dashboardService;
        this.inChargeAuthService = inChargeAuthService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<InChargeDashboardStats>> getDashboardStats(
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            InChargeDashboardStats stats = dashboardService.getDashboardStats(inCharge);
            return ResponseEntity.ok(ApiResponse.success("Stats retrieved successfully", stats));
        } catch (Exception e) {
            log.error("Error fetching dashboard stats", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch stats: " + e.getMessage()));
        }
    }

    @GetMapping("/complaints")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getAssignedComplaints(
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            List<ComplaintResponse> complaints = dashboardService.getAssignedComplaints(inCharge);
            return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
        } catch (Exception e) {
            log.error("Error fetching assigned complaints", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch complaints: " + e.getMessage()));
        }
    }

    @GetMapping("/complaints/by-status")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> getComplaintsByStatus(
            @RequestParam ComplaintStatus status,
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            List<ComplaintResponse> complaints = dashboardService
                    .getAssignedComplaintsByStatus(inCharge, status);

            return ResponseEntity.ok(ApiResponse.success("Complaints retrieved successfully", complaints));
        } catch (Exception e) {
            log.error("Error fetching complaints by status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch complaints: " + e.getMessage()));
        }
    }
}
