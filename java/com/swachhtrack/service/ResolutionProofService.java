package com.swachhtrack.service;

import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.entity.ResolutionProof;
import com.swachhtrack.repository.ComplaintRepository;
import com.swachhtrack.repository.ResolutionProofRepository;
import com.swachhtrack.util.FileStorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class ResolutionProofService {

    private final ResolutionProofRepository resolutionProofRepository;
    private final ComplaintRepository complaintRepository;
    private final FileStorageUtil fileStorageUtil;
    private final ComplaintStatusService complaintStatusService;

    @Value("${resolution.proof.max.distance.meters:100}")
    private double maxDistanceMeters;

    public ResolutionProofService(ResolutionProofRepository resolutionProofRepository,
                                  ComplaintRepository complaintRepository,
                                  FileStorageUtil fileStorageUtil,
                                  ComplaintStatusService complaintStatusService) {
        this.resolutionProofRepository = resolutionProofRepository;
        this.complaintRepository = complaintRepository;
        this.fileStorageUtil = fileStorageUtil;
        this.complaintStatusService = complaintStatusService;
    }

    @Transactional
    public ResolutionProof uploadProofAndResolve(
            Long complaintId,
            MultipartFile proofImage,
            Double latitude,
            Double longitude,
            String remarks,
            InCharge inCharge) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        if (complaint.getAssignedTo() == null
                || !complaint.getAssignedTo().getId().equals(inCharge.getId())) {
            throw new RuntimeException("You are not authorized to resolve this complaint");
        }

        if (complaint.getStatus() != ComplaintStatus.IN_PROGRESS) {
            throw new RuntimeException(
                    "Only IN_PROGRESS complaints can be resolved. Current status: "
                            + complaint.getStatus());
        }

        if (resolutionProofRepository.existsByComplaintId(complaintId)) {
            throw new RuntimeException("Resolution proof already exists for this complaint");
        }

        if (latitude == null || longitude == null) {
            throw new RuntimeException("Proof location is required");
        }

        validateProofLocation(complaint, latitude, longitude);

        String imageUrl = fileStorageUtil.storeFile(proofImage);

        ResolutionProof proof = ResolutionProof.builder()
                .complaint(complaint)
                .imageUrl(imageUrl)
                .latitude(latitude)
                .longitude(longitude)
                .uploadedBy(inCharge)
                .remarks(remarks)
                .build();

        proof = resolutionProofRepository.save(proof);

        complaintStatusService.updateComplaintStatus(
                complaintId,
                ComplaintStatus.RESOLVED,
                inCharge,
                "Resolved with proof uploaded");

        log.info("Resolution proof uploaded and complaint {} marked as RESOLVED", complaintId);

        return proof;
    }

    private void validateProofLocation(Complaint complaint, double latitude, double longitude) {
        double distance = haversineMeters(
                complaint.getLatitude(), complaint.getLongitude(), latitude, longitude);
        if (distance > maxDistanceMeters) {
            throw new RuntimeException("Proof location is too far from complaint location (" +
                    Math.round(distance) + "m).");
        }
    }

    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return r * c;
    }

    public ResolutionProof getProofByComplaintId(Long complaintId) {
        return resolutionProofRepository.findByComplaintId(complaintId)
                .orElseThrow(() -> new RuntimeException("Resolution proof not found"));
    }

    public boolean hasProof(Long complaintId) {
        return resolutionProofRepository.existsByComplaintId(complaintId);
    }
}
