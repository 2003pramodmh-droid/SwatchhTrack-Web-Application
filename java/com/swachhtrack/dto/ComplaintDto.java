package com.swachhtrack.dto;

import com.swachhtrack.entity.Complaint.ComplaintStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ComplaintDto {
    private Long id;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private ComplaintStatus status;
    private String assignedTo;
    private String aiGeneratedContent;
    private LocalDateTime escalatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
