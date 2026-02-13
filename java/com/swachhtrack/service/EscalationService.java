package com.swachhtrack.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EscalationService {

    private final ComplaintService complaintService;

    public EscalationService(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void escalatePendingComplaints() {
        complaintService.processEscalations();
    }
}
