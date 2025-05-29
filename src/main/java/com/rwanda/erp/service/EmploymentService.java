package com.rwanda.erp.service;

import com.rwanda.erp.dto.EmploymentRequest;
import com.rwanda.erp.dto.EmploymentResponse;

import java.util.List;

public interface EmploymentService {
    EmploymentResponse createEmployment(EmploymentRequest employmentRequest);
    List<EmploymentResponse> getAllEmployments();
    EmploymentResponse getEmploymentById(String code);
    EmploymentResponse updateEmployment(String code, EmploymentRequest employmentRequest);
    void deleteEmployment(String code);
    List<EmploymentResponse> getEmploymentsByEmployeeCode(String employeeCode);
} 