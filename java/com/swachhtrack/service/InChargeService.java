package com.swachhtrack.service;

import com.swachhtrack.entity.InCharge;
import com.swachhtrack.repository.InChargeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InChargeService {

    private final InChargeRepository repository;

    public InChargeService(InChargeRepository repository) {
        this.repository = repository;
    }

    public List<InCharge> getActiveInCharges() {
        return repository.findByActiveTrue();
    }

    public InCharge create(InCharge inCharge) {
        inCharge.setActive(true);
        return repository.save(inCharge);
    }

    public InCharge getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("In-charge not found"));
    }
}
