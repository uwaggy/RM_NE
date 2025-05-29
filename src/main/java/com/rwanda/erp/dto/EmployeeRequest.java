package com.rwanda.erp.dto;

import com.rwanda.erp.model.Employee.EmployeeStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Set<String> roles;
    private String mobile;
    private LocalDate dateOfBirth;
    private EmployeeStatus status;
} 