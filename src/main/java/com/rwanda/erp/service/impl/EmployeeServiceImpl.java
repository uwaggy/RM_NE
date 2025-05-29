package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.EmployeeRequest;
import com.rwanda.erp.dto.EmployeeResponse;
import com.rwanda.erp.model.Employee;
import com.rwanda.erp.repository.EmployeeRepository;
import com.rwanda.erp.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new RuntimeException("Email already exists"); // TODO: Custom exception handling
        }

        Employee employee = new Employee();
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        employee.setRoles(employeeRequest.getRoles());
        employee.setMobile(employeeRequest.getMobile());
        employee.setDateOfBirth(employeeRequest.getDateOfBirth());
        employee.setStatus(employeeRequest.getStatus());

        Employee savedEmployee = employeeRepository.save(employee);
        return mapEmployeeToEmployeeResponse(savedEmployee);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapEmployeeToEmployeeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeResponse getEmployeeById(String code) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employee not found")); // TODO: Custom exception handling
        return mapEmployeeToEmployeeResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(String code, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employee not found")); // TODO: Custom exception handling

        // Update fields from employeeRequest
        employee.setFirstName(employeeRequest.getFirstName());
        employee.setLastName(employeeRequest.getLastName());
        employee.setRoles(employeeRequest.getRoles());
        employee.setMobile(employeeRequest.getMobile());
        employee.setDateOfBirth(employeeRequest.getDateOfBirth());
        employee.setStatus(employeeRequest.getStatus());

        // Password and email are typically not updated via this method, or require separate processes.
        // For simplicity, I'm excluding them here. Adjust as needed based on requirements.

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapEmployeeToEmployeeResponse(updatedEmployee);
    }

    @Override
    public void deleteEmployee(String code) {
        employeeRepository.deleteById(code);
    }

    private EmployeeResponse mapEmployeeToEmployeeResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setCode(employee.getCode());
        response.setFirstName(employee.getFirstName());
        response.setLastName(employee.getLastName());
        response.setEmail(employee.getEmail());
        response.setRoles(employee.getRoles());
        response.setMobile(employee.getMobile());
        response.setDateOfBirth(employee.getDateOfBirth());
        response.setStatus(employee.getStatus());
        return response;
    }
} 