package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.ResolutionProofResponse;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.entity.ResolutionProof;
import com.swachhtrack.service.InChargeAuthService;
import com.swachhtrack.service.ResolutionProofService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/incharge/resolution-proof")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class ResolutionProofController {

    private final ResolutionProofService resolutionProofService;
    private final InChargeAuthService inChargeAuthService;

    public ResolutionProofController(ResolutionProofService resolutionProofService,
                                     InChargeAuthService inChargeAuthService) {
        this.resolutionProofService = resolutionProofService;
        this.inChargeAuthService = inChargeAuthService;
    }

    @PostMapping(value = "/{complaintId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResolutionProofResponse>> uploadProof(
            @PathVariable Long complaintId,
            @RequestParam("proofImage") MultipartFile proofImage,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "remarks", required = false) String remarks,
            Authentication authentication) {
        try {
            String mobileNumber = authentication.getName();
            InCharge inCharge = inChargeAuthService.getInChargeByMobileNumber(mobileNumber);

            ResolutionProof proof = resolutionProofService.uploadProofAndResolve(
                    complaintId, proofImage, latitude, longitude, remarks, inCharge);

            ResolutionProofResponse response = ResolutionProofResponse.builder()
                    .id(proof.getId())
                    .complaintId(proof.getComplaint().getId())
                    .imageUrl(proof.getImageUrl())
                    .latitude(proof.getLatitude())
                    .longitude(proof.getLongitude())
                    .remarks(proof.getRemarks())
                    .uploadedByName(proof.getUploadedBy().getName())
                    .uploadedAt(proof.getUploadedAt())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Proof uploaded and complaint resolved", response));
        } catch (Exception e) {
            log.error("Error uploading resolution proof", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to upload proof: " + e.getMessage()));
        }
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<ApiResponse<ResolutionProofResponse>> getProof(
            @PathVariable Long complaintId) {
        try {
            ResolutionProof proof = resolutionProofService.getProofByComplaintId(complaintId);

            ResolutionProofResponse response = ResolutionProofResponse.builder()
                    .id(proof.getId())
                    .complaintId(proof.getComplaint().getId())
                    .imageUrl(proof.getImageUrl())
                    .latitude(proof.getLatitude())
                    .longitude(proof.getLongitude())
                    .uploadedByName(proof.getUploadedBy().getName())
                    .remarks(proof.getRemarks())
                    .uploadedAt(proof.getUploadedAt())
                    .build();

            return ResponseEntity.ok(ApiResponse.success("Proof retrieved successfully", response));
        } catch (Exception e) {
            log.error("Error fetching resolution proof", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Proof not found: " + e.getMessage()));
        }
    }
}
