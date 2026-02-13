package com.swachhtrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InChargeDashboardStats {
    private Long totalComplaints;
    private Long pending;
    private Long inProgress;
    private Long resolved;
    private Long escalated;
}
