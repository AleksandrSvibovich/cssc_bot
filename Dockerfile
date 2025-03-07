# Этап сборки
FROM openjdk:23-jdk-slim AS build

# Установка Maven и unzip
RUN apt-get update && apt-get install -y \
                                    maven \
                                    curl \
                                    && apt-get clean
# Установка Node.js и npm
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
                                                        apt-get install -y nodejs
# Установка необходимых библиотек для Playwright
RUN apt-get install -y \
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libxkbcommon0 \
    libxdamage1 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2 \
    libatspi2.0-0

WORKDIR /app
COPY . /app

# Установка зависимостей проекта
RUN npm install playwright

# Установка браузеров Playwright
RUN npx playwright install

RUN mvn clean package

# Финальный этап
FROM openjdk:23-jdk-slim

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot.jar
COPY ./config.properties /app/config.properties

CMD ["java", "-cp", "my-Bot.jar:lib/*", "bot.Tgbot", "--logging.level.root=DEBUG"]
