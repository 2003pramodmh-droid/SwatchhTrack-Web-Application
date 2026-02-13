package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.ComplaintResponse;
import com.swachhtrack.dto.StatusUpdateRequest;
import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.service.ComplaintService;
import com.swachhtrack.service.ComplaintStatusService;
import com.swachhtrack.service.InChargeAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incharge/complaints")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class InChargeComplaintController {

    private final ComplaintStatusService complaintStatusService;
    private final InChargeAuthService inChargeAuthService;
    private final ComplaintService complaintService;

    public InChargeComplaintController(ComplaintStatusService complaintStatusService,
                                       InChargeAuthService inChargeAuthService,
                                       ComplaintService complaintService) {
        this.complaintStatusService = complaintStatusService;
        this.inChargeAuthService = inChargeAuthService;
        this.complaintService = complaintService;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ComplaintResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request,
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            Complaint complaint = complaintStatusService.updateComplaintStatus(
                    id, request.getStatus(), inCharge, request.getRemarks());

            ComplaintResponse response = complaintService.mapToResponse(complaint);
            return ResponseEntity.ok(ApiResponse.success("Status updated successfully", response));
        } catch (Exception e) {
            log.error("Error updating complaint status", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update status: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getComplaint(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            Complaint complaintEntity = complaintService.getEntityById(id);
            if (complaintEntity.getAssignedTo() == null
                    || !complaintEntity.getAssignedTo().getId().equals(inCharge.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error("Access denied to this complaint"));
            }

            return ResponseEntity.ok(ApiResponse.success(
                    "Complaint retrieved",
                    complaintService.mapToResponse(complaintEntity)));
        } catch (Exception e) {
            log.error("Error fetching complaint", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }
}
