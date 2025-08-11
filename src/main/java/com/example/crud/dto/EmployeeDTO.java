package com.example.crud.dto;

import io.micronaut.core.annotation.Introspected;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Introspected
@Schema(name = "EmployeeDTO", description = "Данные сотрудника")
public class EmployeeDTO {

    @Schema(
            description = "Уникальный идентификатор сотрудника (генерируется автоматически)",
            example = "100",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Schema(
            description = "Имя сотрудника",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Иван"
    )
    private String firstName;

    @NotBlank(message = "Фамилия обязательна для заполнения")
    @Size(min = 2, max = 50, message = "Фамилия должна содержать от 2 до 50 символов")
    @Schema(
            description = "Фамилия сотрудника",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "Иванов"
    )
    private String lastName;

    @Size(max = 50, message = "Отчество не должно превышать 50 символов")
    @Schema(
            description = "Отчество сотрудника (необязательное поле)",
            example = "Петрович"
    )
    private String middleName;

    @NotBlank(message = "Логин обязателен для заполнения")
    @Size(min = 3, max = 30, message = "Логин должен содержать от 3 до 30 символов")
    @Schema(
            description = "Уникальный логин для входа в систему",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "ivanov"
    )
    private String login;

    @NotBlank(message = "Пароль обязателен для заполнения")
    @Size(min = 6, max = 100, message = "Пароль должен содержать от 6 до 100 символов")
    @Schema(
            description = "Пароль (минимум 6 символов)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "strongPassword123!"
    )
    private String password;

    @NotBlank(message = "Email обязателен для заполнения")
    @Email(message = "Некорректный формат email")
    @Schema(
            description = "Электронная почта (уникальная для каждого сотрудника)",
            requiredMode = Schema.RequiredMode.REQUIRED,
            example = "ivanov@example.com"
    )
    private String email;

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}