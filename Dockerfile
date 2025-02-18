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
COPY --from=build /app/target/myBot.jar /app/myBot.jar

CMD ["java", "-jar", "myBot.jar"]
