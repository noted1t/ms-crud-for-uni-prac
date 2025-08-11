package com.example.crud.controller;

import com.example.crud.domain.Employee;
import com.example.crud.dto.EmployeeDTO;
import com.example.crud.repository.EmployeeRepository;
import com.example.crud.service.PasswordEncoder;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller("/employees")
@ExecuteOn(TaskExecutors.IO)
public class EmployeeController {

    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeController(
            EmployeeRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Get
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Get("/{id}")
    public Optional<Employee> getById(Long id) {
        return repository.findById(id);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Employee create(@Body @Valid EmployeeDTO employeeDTO) {
        // Проверка уникальности логина и email
        if (repository.findByLogin(employeeDTO.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Логин уже существует");
        }
        if (repository.findByEmail(employeeDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Такая электронная почта уже есть в базе");
        }

        Employee employee = new Employee();
        mapDtoToEntity(employeeDTO, employee);
        return repository.save(employee);
    }

    @Put("/{id}")
    public Employee update(Long id, @Body @Valid EmployeeDTO employeeDTO) {
        Employee employee = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Сотрудник не был найден"));

        // Сохраняем текущие значения для проверки изменений
        String currentLogin = employee.getLogin();
        String currentEmail = employee.getEmail();

        mapDtoToEntity(employeeDTO, employee);

        // Проверка уникальности при изменении логина/email
        if (!currentLogin.equals(employee.getLogin())) {
            repository.findByLogin(employee.getLogin()).ifPresent(e -> {
                if (!e.getId().equals(id)) {
                    throw new IllegalArgumentException("Пользователь с таким логином уже существует");
                }
            });
        }

        if (!currentEmail.equals(employee.getEmail())) {
            repository.findByEmail(employee.getEmail()).ifPresent(e -> {
                if (!e.getId().equals(id)) {
                    throw new IllegalArgumentException("Пользователь с такой электронной почтой уже есть");
                }
            });
        }

        return repository.update(employee);
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void mapDtoToEntity(EmployeeDTO dto, Employee entity) {
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setMiddleName(dto.getMiddleName());
        entity.setLogin(dto.getLogin());
        entity.setEmail(dto.getEmail());

        // Хеширование пароля при его изменении
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
    }
}