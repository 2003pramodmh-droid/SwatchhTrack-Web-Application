package com.swachhtrack.repository;

import com.swachhtrack.entity.Complaint;
import com.swachhtrack.entity.Complaint.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Complaint> findByStatusOrderByCreatedAtDesc(ComplaintStatus status);

    @Query("SELECT c FROM Complaint c WHERE c.status = 'PENDING' AND c.createdAt < :thresholdTime")
    List<Complaint> findPendingComplaintsOlderThan(@Param("thresholdTime") LocalDateTime thresholdTime);

    List<Complaint> findByAssignedToId(Long inchargeId);

    List<Complaint> findByAssignedToIdOrderByCreatedAtDesc(Long inchargeId);

    Long countByAssignedToId(Long inchargeId);

    Long countByAssignedToIdAndStatus(Long inchargeId, ComplaintStatus status);
}
