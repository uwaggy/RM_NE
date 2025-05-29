package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.EmploymentRequest;
import com.rwanda.erp.dto.EmploymentResponse;
import com.rwanda.erp.model.Employee;
import com.rwanda.erp.model.Employment;
import com.rwanda.erp.repository.EmployeeRepository;
import com.rwanda.erp.repository.EmploymentRepository;
import com.rwanda.erp.service.EmploymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmploymentServiceImpl implements EmploymentService {

    private final EmploymentRepository employmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmploymentResponse createEmployment(EmploymentRequest employmentRequest) {
        Employee employee = employeeRepository.findById(employmentRequest.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Employee not found")); // TODO: Custom exception handling

        Employment employment = new Employment();
        employment.setEmployee(employee);
        employment.setDepartment(employmentRequest.getDepartment());
        employment.setPosition(employmentRequest.getPosition());
        employment.setBaseSalary(employmentRequest.getBaseSalary());
        employment.setStatus(employmentRequest.getStatus());
        employment.setJoiningDate(employmentRequest.getJoiningDate());

        Employment savedEmployment = employmentRepository.save(employment);
        return mapEmploymentToEmploymentResponse(savedEmployment);
    }

    @Override
    public List<EmploymentResponse> getAllEmployments() {
        return employmentRepository.findAll().stream()
                .map(this::mapEmploymentToEmploymentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public EmploymentResponse getEmploymentById(String code) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found")); // TODO: Custom exception handling
        return mapEmploymentToEmploymentResponse(employment);
    }

    @Override
    public EmploymentResponse updateEmployment(String code, EmploymentRequest employmentRequest) {
        Employment employment = employmentRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Employment not found")); // TODO: Custom exception handling

        Employee employee = employeeRepository.findById(employmentRequest.getEmployeeCode())
                .orElseThrow(() -> new RuntimeException("Employee not found")); // TODO: Custom exception handling

        employment.setEmployee(employee);
        employment.setDepartment(employmentRequest.getDepartment());
        employment.setPosition(employmentRequest.getPosition());
        employment.setBaseSalary(employmentRequest.getBaseSalary());
        employment.setStatus(employmentRequest.getStatus());
        employment.setJoiningDate(employmentRequest.getJoiningDate());

        Employment updatedEmployment = employmentRepository.save(employment);
        return mapEmploymentToEmploymentResponse(updatedEmployment);
    }

    @Override
    public void deleteEmployment(String code) {
        employmentRepository.deleteById(code);
    }

    @Override
    public List<EmploymentResponse> getEmploymentsByEmployeeCode(String employeeCode) {
        return employmentRepository.findByEmployeeCode(employeeCode).stream()
                .map(this::mapEmploymentToEmploymentResponse)
                .collect(Collectors.toList());
    }

    private EmploymentResponse mapEmploymentToEmploymentResponse(Employment employment) {
        EmploymentResponse response = new EmploymentResponse();
        response.setCode(employment.getCode());
        response.setEmployeeCode(employment.getEmployee().getCode());
        response.setDepartment(employment.getDepartment());
        response.setPosition(employment.getPosition());
        response.setBaseSalary(employment.getBaseSalary());
        response.setStatus(employment.getStatus());
        response.setJoiningDate(employment.getJoiningDate());
        return response;
    }
} 