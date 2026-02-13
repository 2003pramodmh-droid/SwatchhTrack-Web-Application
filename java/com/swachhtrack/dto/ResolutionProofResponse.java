package com.swachhtrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolutionProofResponse {
    private Long id;
    private Long complaintId;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String uploadedByName;
    private String remarks;
    private LocalDateTime uploadedAt;
}
