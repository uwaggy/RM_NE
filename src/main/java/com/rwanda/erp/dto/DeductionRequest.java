package com.rwanda.erp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DeductionRequest {
    private String deductionName;
    private BigDecimal percentage;
} 