package com.rwanda.erp.service;

import com.rwanda.erp.dto.MessageResponse;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessagesByEmployeeCode(String employeeCode);
    List<MessageResponse> getAllMessages(Integer month, Integer year);
    MessageResponse getMessageById(String id);
} 