package com.swachhtrack.dto;

import com.swachhtrack.entity.Complaint.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponse {
    private Long id;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private ComplaintStatus status;
    private String assignedToName;
    private String assignedToWard;
    private String assignedToDesignation;
    private String aiGeneratedContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime escalatedAt;
    private String lastUpdatedBy;
    private LocalDateTime lastUpdatedAt;
    private boolean hasResolutionProof;
    private String resolutionProofUrl;
}
