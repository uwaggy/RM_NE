package com.rwanda.erp.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DeductionResponse {
    private String code;
    private String deductionName;
    private BigDecimal percentage;
} 