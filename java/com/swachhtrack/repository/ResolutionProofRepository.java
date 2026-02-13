package com.swachhtrack.repository;

import com.swachhtrack.entity.ResolutionProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResolutionProofRepository extends JpaRepository<ResolutionProof, Long> {
    Optional<ResolutionProof> findByComplaintId(Long complaintId);
    boolean existsByComplaintId(Long complaintId);
}
