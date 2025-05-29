package com.rwanda.erp.dto;

import com.rwanda.erp.model.Payslip.PayslipStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PayslipResponse {
    private String id;
    private String employeeCode;
    private BigDecimal houseAmount;
    private BigDecimal transportAmount;
    private BigDecimal employeeTaxedAmount;
    private BigDecimal pensionAmount;
    private BigDecimal medicalInsuranceAmount;
    private BigDecimal otherTaxedAmount;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private Integer month;
    private Integer year;
    private PayslipStatus status;
} 