package com.rwanda.erp.dto;

import com.rwanda.erp.model.Employment.EmploymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmploymentRequest {
    private String employeeCode;
    private String department;
    private String position;
    private BigDecimal baseSalary;
    private EmploymentStatus status;
    private LocalDate joiningDate;
} 