package com.swachhtrack.entity;

import com.swachhtrack.entity.Complaint.ComplaintStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus newStatus;

    @Column(nullable = false, length = 20)
    private String updatedBy;

    @Column(nullable = false)
    private String updatedByName;

    @Column(nullable = false)
    private Long updatedById;

    @Column(length = 500)
    private String remarks;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
