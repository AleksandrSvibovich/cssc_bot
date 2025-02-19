# Этап сборки
FROM openjdk:21-jdk-slim AS build

# Установка Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package
RUN ls -l /app/target  # Проверка содержимого
RUN jar tf /app/target/*.jar  # Проверка содержимого JAR-файла

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot-jar-with-dependencies.jar
RUN ls -l /app  # Проверка содержимого
RUN jar tf /app/my-Bot-jar-with-dependencies.jar  # Проверка содержимого JAR-файла

CMD ["java", "-jar", "/app/my-Bot-jar-with-dependencies.jar", "--logging.level.root=DEBUG"]
