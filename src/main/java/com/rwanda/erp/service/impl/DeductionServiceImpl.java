package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.DeductionRequest;
import com.rwanda.erp.dto.DeductionResponse;
import com.rwanda.erp.model.Deduction;
import com.rwanda.erp.repository.DeductionRepository;
import com.rwanda.erp.service.DeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeductionServiceImpl implements DeductionService {

    private final DeductionRepository deductionRepository;

    @Override
    public DeductionResponse createDeduction(DeductionRequest deductionRequest) {
        if (deductionRepository.findByDeductionName(deductionRequest.getDeductionName()).isPresent()) {
            throw new RuntimeException("Deduction with this name already exists"); // TODO: Custom exception
        }
        Deduction deduction = new Deduction();
        deduction.setDeductionName(deductionRequest.getDeductionName());
        deduction.setPercentage(deductionRequest.getPercentage());
        Deduction savedDeduction = deductionRepository.save(deduction);
        return mapDeductionToDeductionResponse(savedDeduction);
    }

    @Override
    public List<DeductionResponse> getAllDeductions() {
        return deductionRepository.findAll().stream()
                .map(this::mapDeductionToDeductionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeductionResponse getDeductionById(String code) {
        Deduction deduction = deductionRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found")); // TODO: Custom exception
        return mapDeductionToDeductionResponse(deduction);
    }

    @Override
    public DeductionResponse updateDeduction(String code, DeductionRequest deductionRequest) {
        Deduction deduction = deductionRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Deduction not found")); // TODO: Custom exception

        // Check if deduction name is being changed and if the new name already exists
        if (!deduction.getDeductionName().equals(deductionRequest.getDeductionName())) {
             if (deductionRepository.findByDeductionName(deductionRequest.getDeductionName()).isPresent()) {
                throw new RuntimeException("Deduction with this name already exists"); // TODO: Custom exception
            }
             deduction.setDeductionName(deductionRequest.getDeductionName());
        }

        deduction.setPercentage(deductionRequest.getPercentage());
        Deduction updatedDeduction = deductionRepository.save(deduction);
        return mapDeductionToDeductionResponse(updatedDeduction);
    }

    @Override
    public void deleteDeduction(String code) {
        deductionRepository.deleteById(code);
    }

    @Override
    public void initializeDefaultDeductions() {
        createDeductionIfNotExists("Employee Tax", BigDecimal.valueOf(30));
        createDeductionIfNotExists("Pension", BigDecimal.valueOf(6));
        createDeductionIfNotExists("Medical Insurance", BigDecimal.valueOf(5));
        createDeductionIfNotExists("Others", BigDecimal.valueOf(5));
        createDeductionIfNotExists("Housing", BigDecimal.valueOf(14));
        createDeductionIfNotExists("Transport", BigDecimal.valueOf(14));
    }

    private void createDeductionIfNotExists(String name, BigDecimal percentage) {
        Optional<Deduction> existingDeduction = deductionRepository.findByDeductionName(name);
        if (existingDeduction.isEmpty()) {
            Deduction deduction = new Deduction();
            deduction.setDeductionName(name);
            deduction.setPercentage(percentage);
            deductionRepository.save(deduction);
        }
    }

    private DeductionResponse mapDeductionToDeductionResponse(Deduction deduction) {
        DeductionResponse response = new DeductionResponse();
        response.setCode(deduction.getCode());
        response.setDeductionName(deduction.getDeductionName());
        response.setPercentage(deduction.getPercentage());
        return response;
    }
}