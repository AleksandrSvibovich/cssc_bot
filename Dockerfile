# Этап сборки
FROM openjdk:21-jdk-slim AS build

# Установка Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package
RUN ls -l /app/target  # Проверка содержимого

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot-jar-with-dependencies.jar
RUN ls -l /app  # Проверка содержимого

CMD ["java", "-jar", "/app/my-Bot-jar-with-dependencies.jar", "--logging.level.root=DEBUG"]
