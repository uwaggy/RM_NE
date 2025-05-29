package com.rwanda.erp.repository;

import com.rwanda.erp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByEmployeeCode(String employeeCode);
    List<Message> findByMonthAndYear(Integer month, Integer year);
} 