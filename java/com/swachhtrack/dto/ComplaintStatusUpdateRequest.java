package com.swachhtrack.dto;

import com.swachhtrack.entity.Complaint.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintStatusUpdateRequest {
    @NotNull
    private ComplaintStatus status;
}
