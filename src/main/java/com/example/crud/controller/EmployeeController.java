package com.example.crud.controller;

import com.example.crud.domain.Employee;
import com.example.crud.dto.EmployeeDTO;
import com.example.crud.repository.EmployeeRepository;
import com.example.crud.service.PasswordEncoder;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller("/employees")
@ExecuteOn(TaskExecutors.IO)
@Tag(name = "Сотрудники", description = "Управление данными сотрудников")
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
    @Operation(
            summary = "Получить всех сотрудников",
            description = "Возвращает полный список сотрудников в системе"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение списка сотрудников",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Employee.class, type = "array"))
    )
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @Get("/{id}")
    @Operation(
            summary = "Получить сотрудника по ID",
            description = "Возвращает данные конкретного сотрудника по его идентификатору"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Сотрудник найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Employee.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Сотрудник с указанным ID не найден"
    )
    public Optional<Employee> getById(
            @Parameter(
                    name = "id",
                    description = "Идентификатор сотрудника",
                    required = true,
                    in = ParameterIn.PATH
            )
            Long id
    ) {
        return repository.findById(id);
    }

    @Post
    @Status(HttpStatus.CREATED)
    @Operation(
            summary = "Создать нового сотрудника",
            description = "Добавляет нового сотрудника в систему. Логин и email должны быть уникальными."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Сотрудник успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Employee.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные входные данные или нарушение уникальности"
    )
    public Employee create(
            @RequestBody(
                    description = "Данные нового сотрудника",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmployeeDTO.class)))
            @Body @Valid EmployeeDTO employeeDTO
    ) {
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
    @Operation(
            summary = "Обновить данные сотрудника",
            description = "Обновляет информацию о существующем сотруднике. При изменении логина/email проверяется уникальность."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Данные сотрудника успешно обновлены",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Employee.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Некорректные данные или нарушение уникальности"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Сотрудник с указанным ID не найден"
    )
    public Employee update(
            @Parameter(
                    name = "id",
                    description = "Идентификатор сотрудника для обновления",
                    required = true,
                    in = ParameterIn.PATH
            )
            Long id,

            @RequestBody(
                    description = "Обновленные данные сотрудника",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EmployeeDTO.class)))
            @Body @Valid EmployeeDTO employeeDTO
    ) {
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
    @Operation(
            summary = "Удалить сотрудника",
            description = "Удаляет сотрудника из системы по его идентификатору"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Сотрудник успешно удален"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Сотрудник с указанным ID не найден"
    )
    public void delete(
            @Parameter(
                    name = "id",
                    description = "Идентификатор сотрудника для удаления",
                    required = true,
                    in = ParameterIn.PATH
            )
            Long id
    ) {
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