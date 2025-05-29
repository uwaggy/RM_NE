package com.rwanda.erp.controller;

import com.rwanda.erp.dto.EmploymentRequest;
import com.rwanda.erp.dto.EmploymentResponse;
import com.rwanda.erp.service.EmploymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employments")
@RequiredArgsConstructor
@Tag(name = "Employment Management", description = "APIs for managing employee employment details")
public class EmploymentController {

    private final EmploymentService employmentService;

    @Operation(summary = "Create new employment for an employee", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmploymentResponse> createEmployment(@RequestBody EmploymentRequest employmentRequest) {
        EmploymentResponse createdEmployment = employmentService.createEmployment(employmentRequest);
        return new ResponseEntity<>(createdEmployment, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all employments", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of employments"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmploymentResponse>> getAllEmployments() {
        List<EmploymentResponse> employments = employmentService.getAllEmployments();
        return ResponseEntity.ok(employments);
    }

    @Operation(summary = "Get employment by code", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employment"),
            @ApiResponse(responseCode = "404", description = "Employment not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmploymentResponse> getEmploymentById(@PathVariable String code) {
        EmploymentResponse employment = employmentService.getEmploymentById(code);
        return ResponseEntity.ok(employment);
    }

    @Operation(summary = "Update an employment", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Employment not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<EmploymentResponse> updateEmployment(@PathVariable String code, @RequestBody EmploymentRequest employmentRequest) {
        EmploymentResponse updatedEmployment = employmentService.updateEmployment(code, employmentRequest);
        return ResponseEntity.ok(updatedEmployment);
    }

    @Operation(summary = "Delete an employment", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employment not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @DeleteMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteEmployment(@PathVariable String code) {
        employmentService.deleteEmployment(code);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all employments for a specific employee", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of employments"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<EmploymentResponse>> getEmploymentsByEmployeeCode(@PathVariable String employeeCode) {
        List<EmploymentResponse> employments = employmentService.getEmploymentsByEmployeeCode(employeeCode);
        return ResponseEntity.ok(employments);
    }
} 