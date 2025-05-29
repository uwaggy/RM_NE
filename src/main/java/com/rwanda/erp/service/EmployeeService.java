package com.rwanda.erp.service;

import com.rwanda.erp.dto.EmployeeRequest;
import com.rwanda.erp.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
    List<EmployeeResponse> getAllEmployees();
    EmployeeResponse getEmployeeById(String code);
    EmployeeResponse updateEmployee(String code, EmployeeRequest employeeRequest);
    void deleteEmployee(String code);
} 