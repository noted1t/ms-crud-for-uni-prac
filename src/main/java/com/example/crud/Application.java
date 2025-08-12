package com.example.crud;

import com.example.crud.repository.EmployeeRepository;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Employee Management API",
                version = "1.0",
                description = "API для управления данными сотрудников",
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT"),
                contact = @Contact(url = "https://example.com", name = "Support", email = "support@example.com")
        )
)
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = Micronaut.run(Application.class, args);
        EmployeeRepository repo = context.getBean(EmployeeRepository.class);
    }
}