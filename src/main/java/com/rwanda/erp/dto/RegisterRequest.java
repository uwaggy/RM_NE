package com.rwanda.erp.dto;

import com.rwanda.erp.model.Employee.EmployeeStatus;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@Data
public class RegisterRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private Set<String> roles;

    @NotBlank
    private String mobile;

    private LocalDate dateOfBirth;

    private EmployeeStatus status = EmployeeStatus.ACTIVE; // Default to active
} 