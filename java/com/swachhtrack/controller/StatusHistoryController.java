package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.dto.StatusHistoryResponse;
import com.swachhtrack.service.StatusHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints/{complaintId}/history")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class StatusHistoryController {

    private final StatusHistoryService statusHistoryService;

    public StatusHistoryController(StatusHistoryService statusHistoryService) {
        this.statusHistoryService = statusHistoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusHistoryResponse>>> getComplaintHistory(
            @PathVariable Long complaintId) {
        try {
            List<StatusHistoryResponse> history = statusHistoryService
                    .getComplaintHistory(complaintId);

            return ResponseEntity.ok(ApiResponse.success("History retrieved successfully", history));
        } catch (Exception e) {
            log.error("Error fetching complaint history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to fetch history: " + e.getMessage()));
        }
    }
}
