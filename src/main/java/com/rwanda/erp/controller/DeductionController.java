package com.rwanda.erp.controller;

import com.rwanda.erp.dto.DeductionRequest;
import com.rwanda.erp.dto.DeductionResponse;
import com.rwanda.erp.service.DeductionService;
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
@RequestMapping("/api/deductions")
@RequiredArgsConstructor
@Tag(name = "Deduction Management", description = "APIs for managing deduction percentages")
public class DeductionController {

    private final DeductionService deductionService;

    @Operation(summary = "Create a new deduction", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Deduction created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Deduction name already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DeductionResponse> createDeduction(@RequestBody DeductionRequest deductionRequest) {
        DeductionResponse createdDeduction = deductionService.createDeduction(deductionRequest);
        return new ResponseEntity<>(createdDeduction, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all deductions", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of deductions"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<DeductionResponse>> getAllDeductions() {
        List<DeductionResponse> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(deductions);
    }

    @Operation(summary = "Get deduction by code", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved deduction"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DeductionResponse> getDeductionById(@PathVariable String code) {
        DeductionResponse deduction = deductionService.getDeductionById(code);
        return ResponseEntity.ok(deduction);
    }

    @Operation(summary = "Update a deduction", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deduction updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "409", description = "Deduction name already exists"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PutMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DeductionResponse> updateDeduction(@PathVariable String code, @RequestBody DeductionRequest deductionRequest) {
        DeductionResponse updatedDeduction = deductionService.updateDeduction(code, deductionRequest);
        return ResponseEntity.ok(updatedDeduction);
    }

    @Operation(summary = "Delete a deduction", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deduction deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Deduction not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @DeleteMapping("/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteDeduction(@PathVariable String code) {
        deductionService.deleteDeduction(code);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Initialize default deductions", description = "Initializes default deduction types and percentages. Requires ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Default deductions initialized successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PostMapping("/initialize")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> initializeDefaultDeductions() {
        deductionService.initializeDefaultDeductions();
        return ResponseEntity.ok().build();
    }
} 