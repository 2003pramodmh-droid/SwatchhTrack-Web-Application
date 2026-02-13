package com.swachhtrack.controller;

import com.swachhtrack.dto.ApiResponse;
import com.swachhtrack.entity.InCharge;
import com.swachhtrack.service.InChargeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incharges")
public class InChargeController {

    private final InChargeService service;

    public InChargeController(InChargeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll() {
        List<InCharge> list = service.getActiveInCharges();
        return ResponseEntity.ok(new ApiResponse(true, "OK", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        InCharge inCharge = service.getById(id);
        return ResponseEntity.ok(new ApiResponse(true, "OK", inCharge));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody InCharge inCharge) {
        InCharge saved = service.create(inCharge);
        return ResponseEntity.ok(new ApiResponse(true, "Created", saved));
    }
}
