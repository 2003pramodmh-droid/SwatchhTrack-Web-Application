package com.swachhtrack.service;

import com.swachhtrack.dto.ComplaintResponse;
import com.swachhtrack.dto.InChargeDashboardStats;
import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.repository.ComplaintRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InChargeDashboardService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintService complaintService;

    public InChargeDashboardService(ComplaintRepository complaintRepository,
                                    ComplaintService complaintService) {
        this.complaintRepository = complaintRepository;
        this.complaintService = complaintService;
    }

    public InChargeDashboardStats getDashboardStats(InCharge inCharge) {
        Long total = complaintRepository.countByAssignedToId(inCharge.getId());
        Long pending = complaintRepository.countByAssignedToIdAndStatus(
                inCharge.getId(), ComplaintStatus.PENDING);
        Long inProgress = complaintRepository.countByAssignedToIdAndStatus(
                inCharge.getId(), ComplaintStatus.IN_PROGRESS);
        Long resolved = complaintRepository.countByAssignedToIdAndStatus(
                inCharge.getId(), ComplaintStatus.RESOLVED);
        Long escalated = complaintRepository.countByAssignedToIdAndStatus(
                inCharge.getId(), ComplaintStatus.ESCALATED);

        return InChargeDashboardStats.builder()
                .totalComplaints(total)
                .pending(pending)
                .inProgress(inProgress)
                .resolved(resolved)
                .escalated(escalated)
                .build();
    }

    public List<ComplaintResponse> getAssignedComplaints(InCharge inCharge) {
        List<Complaint> complaints = complaintRepository
                .findByAssignedToIdOrderByCreatedAtDesc(inCharge.getId());

        return complaints.stream()
                .map(complaintService::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getAssignedComplaintsByStatus(
            InCharge inCharge, ComplaintStatus status) {

        List<Complaint> complaints = complaintRepository
                .findByAssignedToIdOrderByCreatedAtDesc(inCharge.getId());

        return complaints.stream()
                .filter(c -> c.getStatus() == status)
                .map(complaintService::mapToResponse)
                .collect(Collectors.toList());
    }
}
