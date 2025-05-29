package com.rwanda.erp.service.impl;

import com.rwanda.erp.dto.PayslipResponse;
import com.rwanda.erp.model.Deduction;
import com.rwanda.erp.model.Employment;
import com.rwanda.erp.model.Message;
import com.rwanda.erp.model.Payslip;
import com.rwanda.erp.model.Payslip.PayslipStatus;
import com.rwanda.erp.repository.DeductionRepository;
import com.rwanda.erp.repository.EmploymentRepository;
import com.rwanda.erp.repository.MessageRepository;
import com.rwanda.erp.repository.PayslipRepository;
import com.rwanda.erp.service.PayslipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayslipServiceImpl implements PayslipService {

    private final PayslipRepository payslipRepository;
    private final EmploymentRepository employmentRepository;
    private final DeductionRepository deductionRepository;
    private final MessageRepository messageRepository;

    private static final int SCALE = 2; // For BigDecimal calculations
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Override
    @Transactional
    public List<PayslipResponse> generatePayroll(Integer month, Integer year) {
        // Prevent duplicate payroll generation for the same month and year
        List<Payslip> existingPayslips = payslipRepository.findByMonthAndYear(month, year);
        if (!existingPayslips.isEmpty()) {
            throw new RuntimeException("Payroll already generated for this month and year"); // TODO: Custom exception
        }

        List<Employment> activeEmployments = employmentRepository.findByStatus(Employment.EmploymentStatus.ACTIVE);
        List<Deduction> deductions = deductionRepository.findAll();

        List<Payslip> payslips = activeEmployments.stream()
                .map(employment -> calculatePayslip(employment, deductions, month, year))
                .collect(Collectors.toList());

        List<Payslip> savedPayslips = payslipRepository.saveAll(payslips);

        return savedPayslips.stream()
                .map(this::mapPayslipToPayslipResponse)
                .collect(Collectors.toList());
    }

    private Payslip calculatePayslip(Employment employment, List<Deduction> deductions, Integer month, Integer year) {
        BigDecimal baseSalary = employment.getBaseSalary();

        BigDecimal houseAmount = BigDecimal.ZERO;
        BigDecimal transportAmount = BigDecimal.ZERO;
        BigDecimal employeeTaxedAmount = BigDecimal.ZERO;
        BigDecimal pensionAmount = BigDecimal.ZERO;
        BigDecimal medicalInsuranceAmount = BigDecimal.ZERO;
        BigDecimal otherTaxedAmount = BigDecimal.ZERO;

        for (Deduction deduction : deductions) {
            BigDecimal percentage = deduction.getPercentage().divide(BigDecimal.valueOf(100), SCALE, ROUNDING_MODE);
            BigDecimal deductionAmount = baseSalary.multiply(percentage).setScale(SCALE, ROUNDING_MODE);

            switch (deduction.getDeductionName()) {
                case "Housing":
                    houseAmount = deductionAmount;
                    break;
                case "Transport":
                    transportAmount = deductionAmount;
                    break;
                case "Employee Tax":
                    employeeTaxedAmount = deductionAmount;
                    break;
                case "Pension":
                    pensionAmount = deductionAmount;
                    break;
                case "Medical Insurance":
                    medicalInsuranceAmount = deductionAmount;
                    break;
                case "Others":
                    otherTaxedAmount = deductionAmount;
                    break;
            }
        }

        BigDecimal grossSalary = baseSalary.add(houseAmount).add(transportAmount).setScale(SCALE, ROUNDING_MODE);

        BigDecimal totalDeductions = employeeTaxedAmount.add(pensionAmount).add(medicalInsuranceAmount).add(otherTaxedAmount).setScale(SCALE, ROUNDING_MODE);

        // Ensure deductions do not exceed gross salary
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);
        if (netSalary.compareTo(BigDecimal.ZERO) < 0) {
            netSalary = BigDecimal.ZERO; // Or handle this case as per specific requirements
        }

        Payslip payslip = new Payslip();
        payslip.setEmployee(employment.getEmployee());
        payslip.setHouseAmount(houseAmount);
        payslip.setTransportAmount(transportAmount);
        payslip.setEmployeeTaxedAmount(employeeTaxedAmount);
        payslip.setPensionAmount(pensionAmount);
        payslip.setMedicalInsuranceAmount(medicalInsuranceAmount);
        payslip.setOtherTaxedAmount(otherTaxedAmount);
        payslip.setGrossSalary(grossSalary);
        payslip.setNetSalary(netSalary);
        payslip.setMonth(month);
        payslip.setYear(year);
        payslip.setStatus(PayslipStatus.PENDING);

        return payslip;
    }

    @Override
    public List<PayslipResponse> getAllPayslips(Integer month, Integer year) {
        List<Payslip> payslips;
        if (month != null && year != null) {
            payslips = payslipRepository.findByMonthAndYear(month, year);
        } else if (month != null) {
            // Depending on requirements, you might want to find by month across all years
             throw new UnsupportedOperationException("Finding payslips by month alone is not supported."); // TODO: Implement if needed
        } else if (year != null) {
            // Depending on requirements, you might want to find by year across all months
             throw new UnsupportedOperationException("Finding payslips by year alone is not supported."); // TODO: Implement if needed
        } else {
            payslips = payslipRepository.findAll();
        }
        return payslips.stream()
                .map(this::mapPayslipToPayslipResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PayslipResponse getPayslipById(String id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found")); // TODO: Custom exception
        return mapPayslipToPayslipResponse(payslip);
    }

    @Override
    public List<PayslipResponse> getEmployeePayslips(String employeeCode) {
        return payslipRepository.findByEmployeeCode(employeeCode).stream()
                .map(this::mapPayslipToPayslipResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PayslipResponse approvePayslip(String id) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found")); // TODO: Custom exception

        if (payslip.getStatus() == PayslipStatus.PAID) {
            throw new RuntimeException("Payslip is already paid"); // TODO: Custom exception
        }

        payslip.setStatus(PayslipStatus.PAID);
        Payslip updatedPayslip = payslipRepository.save(payslip);

        // Trigger message creation
        createPayslipMessage(updatedPayslip);

        return mapPayslipToPayslipResponse(updatedPayslip);
    }

    // This method simulates message creation. Actual email sending would be external.
    private void createPayslipMessage(Payslip payslip) {
        String messageText = String.format("Dear %s, your salary for %d/%d amounting to %s has been credited to your account %s successfully.",
                payslip.getEmployee().getFirstName(),
                payslip.getMonth(),
                payslip.getYear(),
                payslip.getNetSalary().toPlainString(),
                payslip.getEmployee().getCode() // Assuming employee code is the account ID
        );

        Message message = new Message();
        message.setEmployee(payslip.getEmployee());
        message.setMessage(messageText);
        message.setMonth(payslip.getMonth());
        message.setYear(payslip.getYear());
        // The sentAt timestamp is set by the entity default value

        messageRepository.save(message);

        // TODO: Integrate with actual email sending mechanism
    }

    @Override
    public byte[] downloadPayslip(String id, String authenticatedUserEmail) {
        Payslip payslip = payslipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payslip not found")); // TODO: Custom exception

        // Access control check: Employee can only download their own payslip
        if (!payslip.getEmployee().getEmail().equals(authenticatedUserEmail)) {
            // Note: ADMIN and MANAGER roles are checked in the controller's @PreAuthorize
             throw new RuntimeException("Access denied: You can only download your own payslip."); // TODO: Custom exception
        }

        // TODO: Implement actual file generation (e.g., PDF, Excel) from payslip data
        // For now, returning a placeholder byte array
        String placeholderContent = "Payslip details for " + payslip.getEmployee().getFirstName() + " - " + payslip.getMonth() + "/" + payslip.getYear();
        return placeholderContent.getBytes();
    }

    private PayslipResponse mapPayslipToPayslipResponse(Payslip payslip) {
        PayslipResponse response = new PayslipResponse();
        response.setId(payslip.getId());
        response.setEmployeeCode(payslip.getEmployee().getCode());
        response.setHouseAmount(payslip.getHouseAmount());
        response.setTransportAmount(payslip.getTransportAmount());
        response.setEmployeeTaxedAmount(payslip.getEmployeeTaxedAmount());
        response.setPensionAmount(payslip.getPensionAmount());
        response.setMedicalInsuranceAmount(payslip.getMedicalInsuranceAmount());
        response.setOtherTaxedAmount(payslip.getOtherTaxedAmount());
        response.setGrossSalary(payslip.getGrossSalary());
        response.setNetSalary(payslip.getNetSalary());
        response.setMonth(payslip.getMonth());
        response.setYear(payslip.getYear());
        response.setStatus(payslip.getStatus());
        return response;
    }
} 