package com.rwanda.erp.controller;

import com.rwanda.erp.dto.MessageResponse;
import com.rwanda.erp.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messaging", description = "APIs for viewing messages")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Get messages for a specific employee", description = "Requires ROLE_EMPLOYEE to view their own messages, or ROLE_ADMIN/MANAGER for any employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee messages"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/employee/{employeeCode}")
     @PreAuthorize("hasRole('EMPLOYEE') and authentication.principal.username == @employeeRepository.findById(#employeeCode).orElse(new com.rwanda.erp.model.Employee()).getEmail() or hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<MessageResponse>> getMessagesByEmployeeCode(@PathVariable String employeeCode) {
        List<MessageResponse> messages = messageService.getMessagesByEmployeeCode(employeeCode);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Get all messages", description = "Requires ROLE_ADMIN or ROLE_MANAGER. If month and year are provided, filters messages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of messages"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<MessageResponse>> getAllMessages(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        List<MessageResponse> messages = messageService.getAllMessages(month, year);
        return ResponseEntity.ok(messages);
    }

     @Operation(summary = "Get message by ID", description = "Requires ROLE_ADMIN or ROLE_MANAGER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved message"),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient privileges")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<MessageResponse> getMessageById(@PathVariable String id) {
        MessageResponse message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }
} 