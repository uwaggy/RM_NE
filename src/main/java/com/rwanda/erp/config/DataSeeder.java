package com.rwanda.erp.config;

import com.rwanda.erp.model.Employee;
import com.rwanda.erp.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Set;
@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {
        // Check if admin already exists
        if (!employeeRepository.existsByEmail("agnes@gmail.com")) {
            Employee admin = new Employee();
            admin.setFirstName("Agnes");
            admin.setLastName("Admin");
            admin.setEmail("agnes@gmail.com");
            admin.setPassword(passwordEncoder.encode("Test@123"));
            admin.setRoles(Set.of("ADMIN"));
            admin.setMobile("+250788888888");
            admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
            admin.setStatus(Employee.EmployeeStatus.ACTIVE);
            employeeRepository.save(admin);
        }
    }
}






