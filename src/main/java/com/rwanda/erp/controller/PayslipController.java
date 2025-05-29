package com.rwanda.erp.controller;

import com.rwanda.erp.dto.PayslipResponse;
import com.rwanda.erp.service.PayslipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payslips")
@RequiredArgsConstructor
@Tag(name = "Payroll and Payslip Management", description = "APIs for generating and viewing payroll/payslips")
public class PayslipController {

    private final PayslipService payslipService;

    @Operation(summary = "Generate payroll for a given month and year", description = "Requires ROLE_MANAGER. Prevents duplicate generation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payroll generated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or payroll already generated"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PostMapping("/generate/{month}/{year}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<PayslipResponse>> generatePayroll(@PathVariable Integer month, @PathVariable Integer year) {
        List<PayslipResponse> payslips = payslipService.generatePayroll(month, year);
        return ResponseEntity.ok(payslips);
    }

    @Operation(summary = "Get all payslips for a given month and year", description = "Requires ROLE_ADMIN or ROLE_MANAGER. If month and year are not provided, returns all payslips.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of payslips"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PayslipResponse>> getAllPayslips(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        List<PayslipResponse> payslips = payslipService.getAllPayslips(month, year);
        return ResponseEntity.ok(payslips);
    }

    @Operation(summary = "Get payslip by ID", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved payslip"),
            @ApiResponse(responseCode = "404", description = "Payslip not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PayslipResponse> getPayslipById(@PathVariable String id) {
        PayslipResponse payslip = payslipService.getPayslipById(id);
        return ResponseEntity.ok(payslip);
    }

    @Operation(summary = "Get payslips for a specific employee", description = "Requires ROLE_EMPLOYEE to view their own payslips, or ROLE_ADMIN/MANAGER for any employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee payslips"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/employee/{employeeCode}")
    @PreAuthorize("hasRole('EMPLOYEE') and authentication.principal.username == @employeeRepository.findById(#employeeCode).orElse(new com.rwanda.erp.model.Employee()).getEmail() or hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<PayslipResponse>> getEmployeePayslips(@PathVariable String employeeCode) {
        List<PayslipResponse> payslips = payslipService.getEmployeePayslips(employeeCode);
        return ResponseEntity.ok(payslips);
    }

    @Operation(summary = "Approve a payslip", description = "Updates payslip status to PAID and triggers message creation. Requires ROLE_ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payslip approved successfully"),
            @ApiResponse(responseCode = "400", description = "Payslip already paid"),
            @ApiResponse(responseCode = "404", description = "Payslip not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PayslipResponse> approvePayslip(@PathVariable String id) {
        PayslipResponse approvedPayslip = payslipService.approvePayslip(id);
        return ResponseEntity.ok(approvedPayslip);
    }
} 