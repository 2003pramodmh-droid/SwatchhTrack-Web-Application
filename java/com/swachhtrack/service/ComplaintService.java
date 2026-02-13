package com.swachhtrack.service;

import com.swachhtrack.dto.ComplaintResponse;
import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.entity.ResolutionProof;
import com.swachhtrack.entity.StatusHistory;
import com.swachhtrack.entity.User;
import com.swachhtrack.repository.ComplaintRepository;
import com.swachhtrack.repository.InChargeRepository;
import com.swachhtrack.repository.StatusHistoryRepository;
import com.swachhtrack.repository.UserRepository;
import com.swachhtrack.util.FileStorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final InChargeRepository inChargeRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final ResolutionProofService resolutionProofService;
    private final AIContentService aiContentService;
    private final FileStorageUtil fileStorageUtil;

    @Value("${complaint.escalation.hours}")
    private int escalationHours;

    public ComplaintService(ComplaintRepository complaintRepository,
                            UserRepository userRepository,
                            InChargeRepository inChargeRepository,
                            StatusHistoryRepository statusHistoryRepository,
                            ResolutionProofService resolutionProofService,
                            AIContentService aiContentService,
                            FileStorageUtil fileStorageUtil) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.inChargeRepository = inChargeRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.resolutionProofService = resolutionProofService;
        this.aiContentService = aiContentService;
        this.fileStorageUtil = fileStorageUtil;
    }

    public ComplaintResponse createComplaint(String mobileNumber,
                                             MultipartFile image,
                                             Double latitude,
                                             Double longitude,
                                             String address) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = fileStorageUtil.storeFile(image);
        InCharge inCharge = inChargeRepository.findByActiveTrue().stream().findFirst().orElse(null);

        Complaint complaint = Complaint.builder()
                .imageUrl(imageUrl)
                .latitude(latitude)
                .longitude(longitude)
                .address(address)
                .status(ComplaintStatus.PENDING)
                .user(user)
                .assignedTo(inCharge)
                .build();

        Complaint saved = complaintRepository.save(complaint);
        return mapToResponse(saved);
    }

    public List<ComplaintResponse> getMyComplaints(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return complaintRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public ComplaintResponse getById(Long id) {
        return complaintRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public Complaint getEntityById(Long id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    public ComplaintResponse updateStatus(Long id, ComplaintStatus status) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus(status);
        Complaint saved = complaintRepository.save(complaint);
        return mapToResponse(saved);
    }

    public List<Complaint> findPendingOlderThan(LocalDateTime threshold) {
        return complaintRepository.findPendingComplaintsOlderThan(threshold);
    }

    public void save(Complaint complaint) {
        complaintRepository.save(complaint);
    }

    @Transactional
    public void processEscalations() {
        LocalDateTime thresholdTime = LocalDateTime.now().minusHours(escalationHours);
        List<Complaint> pendingComplaints = complaintRepository
                .findPendingComplaintsOlderThan(thresholdTime);

        log.info("Processing {} complaints for escalation", pendingComplaints.size());

        for (Complaint complaint : pendingComplaints) {
            try {
                String escalationContent = aiContentService.generateEscalationContent(complaint);

                complaint.setAiGeneratedContent(escalationContent);
                complaint.setStatus(ComplaintStatus.ESCALATED);
                complaint.setEscalatedAt(LocalDateTime.now());

                complaintRepository.save(complaint);

                createSystemStatusHistory(complaint, ComplaintStatus.PENDING, ComplaintStatus.ESCALATED);

                log.info("Complaint {} escalated with AI-generated content", complaint.getId());
            } catch (Exception e) {
                log.error("Failed to escalate complaint {}: {}", complaint.getId(), e.getMessage());
            }
        }
    }

    private void createSystemStatusHistory(
            Complaint complaint,
            ComplaintStatus oldStatus,
            ComplaintStatus newStatus) {

        StatusHistory history = StatusHistory.builder()
                .complaint(complaint)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .updatedBy("SYSTEM")
                .updatedByName("Auto-Escalation System")
                .updatedById(0L)
                .remarks("Automatically escalated after 48 hours of inactivity")
                .timestamp(LocalDateTime.now())
                .build();

        statusHistoryRepository.save(history);
    }

    public ComplaintResponse mapToResponse(Complaint complaint) {
        boolean hasProof = resolutionProofService.hasProof(complaint.getId());
        String proofUrl = null;

        if (hasProof) {
            try {
                ResolutionProof proof = resolutionProofService.getProofByComplaintId(complaint.getId());
                proofUrl = proof.getImageUrl();
            } catch (Exception e) {
                log.warn("Could not fetch proof for complaint {}", complaint.getId());
            }
        }

        StatusHistory lastUpdate = statusHistoryRepository
                .findByComplaintIdOrderByTimestampDesc(complaint.getId())
                .stream()
                .findFirst()
                .orElse(null);

        return ComplaintResponse.builder()
                .id(complaint.getId())
                .imageUrl(complaint.getImageUrl())
                .latitude(complaint.getLatitude())
                .longitude(complaint.getLongitude())
                .address(complaint.getAddress())
                .status(complaint.getStatus())
                .assignedToName(complaint.getAssignedTo() != null ?
                        complaint.getAssignedTo().getName() : null)
                .assignedToWard(complaint.getAssignedTo() != null ?
                        complaint.getAssignedTo().getWard() : null)
                .assignedToDesignation(complaint.getAssignedTo() != null ?
                        complaint.getAssignedTo().getDesignation() : null)
                .aiGeneratedContent(complaint.getAiGeneratedContent())
                .createdAt(complaint.getCreatedAt())
                .updatedAt(complaint.getUpdatedAt())
                .escalatedAt(complaint.getEscalatedAt())
                .lastUpdatedBy(lastUpdate != null ? lastUpdate.getUpdatedByName() : null)
                .lastUpdatedAt(lastUpdate != null ? lastUpdate.getTimestamp() : complaint.getCreatedAt())
                .hasResolutionProof(hasProof)
                .resolutionProofUrl(proofUrl)
                .build();
    }
}
