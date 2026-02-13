package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.ComplaintResponse;
import com.swachhtrack.dto.ComplaintStatusUpdateRequest;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.service.ComplaintService;
import com.swachhtrack.service.ComplaintStatusService;
import com.swachhtrack.service.InChargeAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final ComplaintStatusService complaintStatusService;
    private final InChargeAuthService inChargeAuthService;

    public ComplaintController(ComplaintService complaintService,
                               ComplaintStatusService complaintStatusService,
                               InChargeAuthService inChargeAuthService) {
        this.complaintService = complaintService;
        this.complaintStatusService = complaintStatusService;
        this.inChargeAuthService = inChargeAuthService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ComplaintResponse>> createComplaint(
            Authentication authentication,
            @RequestParam("image") MultipartFile image,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam("address") String address) {

        String mobile = (String) authentication.getPrincipal();
        ComplaintResponse dto = complaintService.createComplaint(mobile, image, latitude, longitude, address);
        return ResponseEntity.ok(ApiResponse.success("Complaint created", dto));
    }

    @GetMapping("/my-complaints")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> myComplaints(Authentication authentication) {
        String mobile = (String) authentication.getPrincipal();
        List<ComplaintResponse> list = complaintService.getMyComplaints(mobile);
        return ResponseEntity.ok(ApiResponse.success("OK", list));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ComplaintResponse>>> allComplaints() {
        List<ComplaintResponse> list = complaintService.getAllComplaints();
        return ResponseEntity.ok(ApiResponse.success("OK", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ComplaintResponse>> getById(@PathVariable Long id) {
        ComplaintResponse dto = complaintService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("OK", dto));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('INCHARGE')")
    public ResponseEntity<ApiResponse<ComplaintResponse>> updateStatus(@PathVariable Long id,
                                                    @Valid @RequestBody ComplaintStatusUpdateRequest request,
                                                    Authentication authentication) {
        String mobileNumber = authentication.getName();
        InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);
        ComplaintResponse dto = complaintService.mapToResponse(
                complaintStatusService.updateComplaintStatus(id, request.getStatus(), inCharge, null));
        return ResponseEntity.ok(ApiResponse.success("Status updated", dto));
    }
}
