# Этап сборки
FROM openjdk:21-jdk-slim AS build

# Установка Maven и unzip
RUN apt-get update && apt-get install -y maven && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package
RUN ls -l /app/target  # Проверка содержимого
RUN jar tf /app/target/*.jar  # Проверка содержимого JAR-файла

# Проверка манифеста
RUN jar xf /app/target/*.jar META-INF/MANIFEST.MF && cat META-INF/MANIFEST.MF

# Финальный этап
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot-jar-with-dependencies.jar
RUN ls -l /app  # Проверка содержимого
RUN jar tf /app/my-Bot-jar-with-dependencies.jar  # Проверка содержимого JAR-файла
RUN jar xf /app/my-Bot-jar-with-dependencies.jar META-INF/MANIFEST.MF && cat META-INF/MANIFEST.MF  # Проверка манифеста

CMD ["java", "-jar", "/app/my-Bot-jar-with-dependencies.jar", "--logging.level.root=DEBUG"]
