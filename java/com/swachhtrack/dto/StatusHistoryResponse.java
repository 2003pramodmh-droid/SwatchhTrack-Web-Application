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
public class StatusHistoryResponse {
    private Long id;
    private ComplaintStatus oldStatus;
    private ComplaintStatus newStatus;
    private String updatedBy;
    private String updatedByName;
    private String remarks;
    private LocalDateTime timestamp;
}
