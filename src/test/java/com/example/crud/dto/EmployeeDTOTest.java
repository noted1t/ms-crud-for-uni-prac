//package com.example.crud.dto;
//
//
//import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
//import jakarta.inject.Inject;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validator;
//import org.junit.jupiter.api.Test;
//
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@MicronautTest
//public class EmployeeDTOTest {
//
//    @Inject
//    Validator validator;
//
//    @Test
//    void shouldBeValidWhenCorrect() {
//        EmployeeDTO dto = createValidDTO();
//        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(dto);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    void shouldRequireFirstName() {
//        EmployeeDTO dto = createValidDTO();
//        dto.setFirstName(null);
//        assertHasViolation(dto, "firstName", "не должно быть пустым");
//    }
//
//    @Test
//    void shouldRequireLastName() {
//        EmployeeDTO dto = createValidDTO();
//        dto.setLastName(null);
//        assertHasViolation(dto, "lastName", "не должно быть пустым");
//    }
//
//    @Test
//    void shouldRequireValidEmail() {
//        EmployeeDTO dto = createValidDTO();
//        dto.setEmail("invalid-email");
//        assertHasViolation(dto, "email", "должно иметь формат");
//    }
//
//    @Test
//    void shouldRequirePasswordMinLength() {
//        EmployeeDTO dto = createValidDTO();
//        dto.setPassword("12345"); // <6 символов
//        assertHasViolation(dto, "password", "размер должен быть");
//    }
//
//    @Test
//    void shouldRequireLogin() {
//        EmployeeDTO dto = createValidDTO();
//        dto.setLogin(null);
//        assertHasViolation(dto, "login", "не должно быть пустым");
//    }
//
//    private void assertHasViolation(EmployeeDTO dto, String property, String messagePart) {
//        Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(dto);
//        assertFalse(violations.isEmpty());
//        assertTrue(violations.stream()
//                .anyMatch(v -> v.getPropertyPath().toString().equals(property) &&
//                        v.getMessage().contains(messagePart)));
//    }
//
//    private EmployeeDTO createValidDTO() {
//        EmployeeDTO dto = new EmployeeDTO();
//        dto.setFirstName("Иван");
//        dto.setLastName("Иванов");
//        dto.setLogin("ivanov");
//        dto.setPassword("securePass123");
//        dto.setEmail("ivanov@example.com");
//        return dto;
//    }
//}