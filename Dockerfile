FROM openjdk:11-jdk-slim
WORKDIR /app
COPY . /app
RUN mvn clean package
CMD ["java", "-jar", "target/myBot.jar"]
