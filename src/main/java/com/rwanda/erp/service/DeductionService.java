package com.rwanda.erp.service;

import com.rwanda.erp.dto.DeductionRequest;
import com.rwanda.erp.dto.DeductionResponse;

import java.util.List;

public interface DeductionService {
    DeductionResponse createDeduction(DeductionRequest deductionRequest);
    List<DeductionResponse> getAllDeductions();
    DeductionResponse getDeductionById(String code);
    DeductionResponse updateDeduction(String code, DeductionRequest deductionRequest);
    void deleteDeduction(String code);
    void initializeDefaultDeductions();
} 