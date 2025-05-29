package com.rwanda.erp.service;

import com.rwanda.erp.dto.PayslipResponse;

import java.util.List;

public interface PayslipService {
    List<PayslipResponse> generatePayroll(Integer month, Integer year);
    List<PayslipResponse> getAllPayslips(Integer month, Integer year);
    PayslipResponse getPayslipById(String id);
    List<PayslipResponse> getEmployeePayslips(String employeeCode);
    PayslipResponse approvePayslip(String id);
} 