package com.rwanda.erp.repository;

import com.rwanda.erp.model.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmploymentRepository extends JpaRepository<Employment, String> {
    List<Employment> findByEmployeeCode(String employeeCode);
    List<Employment> findByStatus(Employment.EmploymentStatus status);
} 