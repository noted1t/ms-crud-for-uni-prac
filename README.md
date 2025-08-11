# microservice CRUD Application

CRUD приложение для управления данными сотрудников с использованием Java и Micronaut.

## Стек
- **Java 17**
- **Micronaut Framework** (версия 4.9.2)
- **Gradle**
- **H2 Database** (in-memory) или **PostgreSQL** (на выбор)
- **Lombok** (для сокращения boilerplate кода)
- **JPA**
- **JUnit** 

## Функционал
- Создание новых сотрудников
- Просмотр списка всех сотрудников
- Поиск сотрудника по ID
- Обновление данных сотрудника
- Удаление сотрудников
- Валидация входных данных
- Хеширование паролей

## Требования
- JDK 17
- Gradle 7.0+
- (Опционально) PostgreSQL, если используется внешняя БД

## Структура проекта

src/

├── main/

│   ├── java/

│   │   └── com/

│   │       └── example/

│   │           └── crud/

│   │               ├── Application.java         # Точка входа

│   │               ├── config/                  # Конфигурации

│   │               ├── controller/              # Контроллеры

│   │               │   └── EmployeeController.java

│   │               ├── domain/                  # Сущности

│   │               │   └── Employee.java

│   │               ├── dto/                     # Data Transfer Objects
│   │               │   └── EmployeeDTO.java

│   │               ├── repository/              # Репозитории
│   │               │   └── EmployeeRepository.java

│   │               └── service/                 # Сервисы
│   │                   └── PasswordEncoder.java

│   └── resources/
│       ├── application.yml                      # Конфигурация

│       └── logback.xml                          # Логирование

└── test/                                        # Тесты

## Быстрый старт

### 1. Клонирование репозитория
```bash
git clone https://github.com/noted1t/ms-crud-for-uni-prac.git
cd ms-crud-for-uni-prac