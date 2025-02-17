FROM openjdk:11-jdk-slim

# Установка Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

WORKDIR /app
COPY . /app
RUN mvn clean package

CMD ["java", "-jar", "target/myBot.jar"]