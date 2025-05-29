package com.rwanda.erp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private String id;
    private String employeeCode;
    private String message;
    private Integer month;
    private Integer year;
    private LocalDateTime sentAt;
} 