# Этап сборки
FROM openjdk:21-jdk-slim AS build

# Установка Maven и unzip
RUN apt-get update && apt-get install -y maven unzip && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package
RUN ls -l /app/target  # Проверка содержимого
RUN jar tf /app/target/*.jar  # Проверка содержимого JAR-файла
RUN unzip -p /app/target/*.jar META-INF/MANIFEST.MF  # Проверка манифеста

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot-jar-with-dependencies.jar
RUN ls -l /app  # Проверка содержимого
RUN jar tf /app/my-Bot-jar-with-dependencies.jar  # Проверка содержимого JAR-файла
RUN unzip -p /app/my-Bot-jar-with-dependencies.jar META-INF/MANIFEST.MF  # Проверка манифеста

CMD ["java", "-jar", "/app/my-Bot-jar-with-dependencies.jar", "--logging.level.root=DEBUG"]
