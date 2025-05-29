package com.rwanda.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Rwanda Government ERP System API",
        version = "1.0",
        description = "API documentation for Rwanda Government ERP System"
    )
)
public class ErpSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ErpSystemApplication.class, args);
    }
} 