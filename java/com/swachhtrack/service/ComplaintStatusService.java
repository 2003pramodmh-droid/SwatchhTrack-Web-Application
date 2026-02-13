package com.swachhtrack.service;

import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.entity.StatusHistory;
import com.swachhtrack.repository.ComplaintRepository;
import com.swachhtrack.repository.ResolutionProofRepository;
import com.swachhtrack.repository.StatusHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ComplaintStatusService {

    private final ComplaintRepository complaintRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final ResolutionProofRepository resolutionProofRepository;

    public ComplaintStatusService(ComplaintRepository complaintRepository,
                                  StatusHistoryRepository statusHistoryRepository,
                                  ResolutionProofRepository resolutionProofRepository) {
        this.complaintRepository = complaintRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.resolutionProofRepository = resolutionProofRepository;
    }

    @Transactional
    public Complaint updateComplaintStatus(
            Long complaintId,
            ComplaintStatus newStatus,
            InCharge inCharge,
            String remarks) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        if (complaint.getAssignedTo() == null
                || !complaint.getAssignedTo().getId().equals(inCharge.getId())) {
            throw new RuntimeException("You are not authorized to update this complaint");
        }

        ComplaintStatus oldStatus = complaint.getStatus();
        validateStatusTransition(oldStatus, newStatus);

        if (newStatus == ComplaintStatus.RESOLVED
                && !resolutionProofRepository.existsByComplaintId(complaintId)) {
            throw new RuntimeException("Resolution proof is required to mark as RESOLVED");
        }

        complaint.setStatus(newStatus);
        complaint = complaintRepository.save(complaint);

        createStatusHistory(complaint, oldStatus, newStatus, inCharge, remarks);

        log.info("Complaint {} status updated from {} to {} by InCharge {}",
                complaintId, oldStatus, newStatus, inCharge.getName());

        return complaint;
    }

    private void validateStatusTransition(ComplaintStatus oldStatus, ComplaintStatus newStatus) {
        if (oldStatus == ComplaintStatus.PENDING && newStatus == ComplaintStatus.IN_PROGRESS) {
            return;
        }

        if (oldStatus == ComplaintStatus.IN_PROGRESS && newStatus == ComplaintStatus.RESOLVED) {
            return;
        }

        if (oldStatus == ComplaintStatus.PENDING && newStatus == ComplaintStatus.RESOLVED) {
            throw new RuntimeException(
                    "Cannot mark complaint as RESOLVED directly. Must be IN_PROGRESS first.");
        }

        if (oldStatus == ComplaintStatus.ESCALATED) {
            throw new RuntimeException(
                    "Escalated complaints cannot be updated. Contact administrator.");
        }

        if (oldStatus == ComplaintStatus.RESOLVED) {
            throw new RuntimeException("Resolved complaints cannot be reopened");
        }

        throw new RuntimeException(
                "Invalid status transition from " + oldStatus + " to " + newStatus);
    }

    private void createStatusHistory(
            Complaint complaint,
            ComplaintStatus oldStatus,
            ComplaintStatus newStatus,
            InCharge inCharge,
            String remarks) {

        StatusHistory history = StatusHistory.builder()
                .complaint(complaint)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy("INCHARGE")
                .updatedByName(inCharge.getName())
                .updatedById(inCharge.getId())
                .remarks(remarks)
                .timestamp(LocalDateTime.now())
                .build();

        statusHistoryRepository.save(history);
    }
}
