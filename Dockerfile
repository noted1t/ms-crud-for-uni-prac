# Этап 1: Сборка приложения с помощью Gradle
FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /home/gradle/src
# Добавьте gradle.properties в эту строку
COPY build.gradle settings.gradle gradle.properties ./
COPY src ./src
RUN gradle shadowJar --no-daemon

# Этап 2: Создание финального образа на основе легковесного JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*-all.jar /app/application.jar
EXPOSE 8000
CMD ["java", "-jar", "/app/application.jar"]