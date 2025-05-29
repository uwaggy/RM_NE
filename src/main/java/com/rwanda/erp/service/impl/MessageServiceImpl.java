package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.MessageResponse;
import com.rwanda.erp.model.Message;
import com.rwanda.erp.repository.MessageRepository;
import com.rwanda.erp.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<MessageResponse> getMessagesByEmployeeCode(String employeeCode) {
        return messageRepository.findByEmployeeCode(employeeCode).stream()
                .map(this::mapMessageToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getAllMessages(Integer month, Integer year) {
         List<Message> messages;
        if (month != null && year != null) {
            messages = messageRepository.findByMonthAndYear(month, year);
        } else if (month != null) {
             // Depending on requirements, you might want to find by month across all years
             throw new UnsupportedOperationException("Finding messages by month alone is not supported."); // TODO: Implement if needed
        } else if (year != null) {
            // Depending on requirements, you might want to find by year across all months
             throw new UnsupportedOperationException("Finding messages by year alone is not supported."); // TODO: Implement if needed
        } else {
            messages = messageRepository.findAll();
        }
        return messages.stream()
                .map(this::mapMessageToMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse getMessageById(String id) {
         Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found")); // TODO: Custom exception
        return mapMessageToMessageResponse(message);
    }

    private MessageResponse mapMessageToMessageResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setEmployeeCode(message.getEmployee().getCode());
        response.setMessage(message.getMessage());
        response.setMonth(message.getMonth());
        response.setYear(message.getYear());
        response.setSentAt(message.getSentAt());
        return response;
    }
} 