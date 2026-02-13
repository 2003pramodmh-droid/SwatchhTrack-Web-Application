package com.swachhtrack.service;

import com.swachhtrack.dto.StatusHistoryResponse;
import com.swachhtrack.entity.StatusHistory;
import com.swachhtrack.repository.StatusHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatusHistoryService {

    private final StatusHistoryRepository statusHistoryRepository;

    public StatusHistoryService(StatusHistoryRepository statusHistoryRepository) {
        this.statusHistoryRepository = statusHistoryRepository;
    }

    public List<StatusHistoryResponse> getComplaintHistory(Long complaintId) {
        List<StatusHistory> history = statusHistoryRepository
                .findByComplaintIdOrderByTimestampDesc(complaintId);

        return history.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private StatusHistoryResponse mapToResponse(StatusHistory history) {
        return StatusHistoryResponse.builder()
                .id(history.getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .updatedBy(history.getUpdatedBy())
                .updatedByName(history.getUpdatedByName())
                .remarks(history.getRemarks())
                .timestamp(history.getTimestamp())
                .build();
    }
}
