package com.rwanda.erp.repository;

import com.rwanda.erp.model.Deduction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeductionRepository extends JpaRepository<Deduction, String> {
    Optional<Deduction> findByDeductionName(String deductionName);
} 