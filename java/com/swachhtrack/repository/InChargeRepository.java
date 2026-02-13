package com.swachhtrack.repository;

import com.swachhtrack.entity.InCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InChargeRepository extends JpaRepository<InCharge, Long> {
    Optional<InCharge> findByWardAndActiveTrue(String ward);
    List<InCharge> findByActiveTrue();
    Optional<InCharge> findByMobileNumber(String mobileNumber);
    Optional<InCharge> findByEmail(String email);
    Optional<InCharge> findByEmployeeId(String employeeId);
    boolean existsByMobileNumber(String mobileNumber);
    boolean existsByEmail(String email);
    boolean existsByEmployeeId(String employeeId);
}
