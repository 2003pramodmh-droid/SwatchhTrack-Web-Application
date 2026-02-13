package com.swachhtrack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "incharges")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String ward;

    @Column(nullable = false, length = 100)
    private String designation;

    @Column(nullable = false, unique = true, length = 10)
    private String mobileNumber;

    @Column(length = 100, unique = true)
    private String email;

    @Column(unique = true, length = 50)
    private String employeeId;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean verified = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
