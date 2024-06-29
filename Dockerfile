# Используем Gradle для сборки проекта
FROM gradle:latest AS build

# Копируем все файлы проекта в рабочую директорию контейнера
COPY --chown=gradle:gradle . /home/gradle/src

# Устанавливаем рабочую директорию
WORKDIR /home/gradle/src

# Сборка проекта без выполнения тестов
RUN gradle build --no-daemon -x test

# Используем минимальный образ с Java 17 JDK
FROM openjdk:17-jdk-slim

# Открываем порт 8080
EXPOSE 8080

# Создаем директорию для приложения
RUN mkdir /app

# Копируем собранные JAR файлы из предыдущего этапа в директорию /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/

# Устанавливаем команду запуска
ENTRYPOINT ["java", "-jar", "/app/spring-boot-application.jar"]
