# Этап сборки
FROM openjdk:23-jdk AS build

# Установка необходимых пакетов
RUN apt-get update && apt-get install -y \
    maven \
    curl \
    gnupg2 \
    && apt-get clean

# Установка Node.js и npm
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash - && \
    apt-get install -y nodejs \
    && apt-get clean

# Установка необходимых библиотек для Playwright
RUN apt-get update && apt-get install -y --no-install-recommends \
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
    libatspi2.0-0 \
    libglib2.0-0 \
    libgobject-2.0-0 \
    libdbus-1-3 \
    libdrm2 \
    libexpat1 \
    libxcb1 \
    libx11-6 \
    libxcomposite1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    && apt-get clean

# Увеличение тайм-аута для установки браузеров
ENV PLAYWRIGHT_BROWSERS_TIMEOUT=600000

WORKDIR /app
COPY . /app

# Установка зависимостей проекта
RUN npm install playwright

# Установка браузеров Playwright
RUN npx playwright install

# Сборка Java-приложения
RUN mvn clean package

# Финальный этап
FROM openjdk:23-jdk

WORKDIR /app
COPY --from=build /app/target/*.jar /app/my-Bot.jar
COPY ./config.properties /app/config.properties

# Проверка доступа к интернету
RUN curl -I https://playwright.azureedge.net

CMD ["java", "-cp", "my-Bot.jar:lib/*", "bot.Tgbot", "--logging.level.root=DEBUG"]
