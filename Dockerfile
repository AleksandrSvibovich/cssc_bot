# Этап сборки
FROM openjdk:21-jdk-slim AS build

# Установка Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app
# Копируем JAR-файл с правильным именем
COPY --from=build /app/target/*.jar /app/my-Bot.jar

CMD ["java", "-jar", "/app/my-Bot.jar", "--logging.level.root=DEBUG"]

