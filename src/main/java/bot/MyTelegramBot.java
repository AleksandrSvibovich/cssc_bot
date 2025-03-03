package bot;

import bot.auth.HealthCheck;
import bot.bd.CreateConnectionBD;
import bot.bd.CreateTablesBD;
import bot.enums.ACCOUNTS;
import bot.enums.URLS;
import bot.getfromenvs.GetFromEnvs;
import bot.keyboards.BotKeyboards;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class MyTelegramBot extends TelegramLongPollingBot {
    GetFromEnvs getFromEnvs = new GetFromEnvs();
    HealthCheck healthCheck = new HealthCheck();
    Connection connection = CreateConnectionBD.getInstance().getConnection();
    List<String> checkAccessList = Arrays.asList(getFromEnvs.getFromEnvsByName("userList").split(" "));

    @Override
    public String getBotUsername() {
        return "sanja_test_bot"; // Укажите имя вашего бота
    }

    @Override
    public String getBotToken() {
        return getFromEnvs.getFromEnvsByName("bot");
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка нажатий на кнопки
        String message;
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            String userName = update.getCallbackQuery().getFrom().getUserName();

            System.out.println("LOGS!!! " + userName + "; Message " + update.getMessage());
            if (!checkAccessList.contains(userName)) {
                sendMessage(chatId, "Sorry, but you don't have permission! Please contact with administrator @sashka_svb");
            }

            switch (callData) {
                case "status_ift":
                    try {
                        sendMessage(chatId, "Проверяю авторизацию");
                        message = healthCheck.checkAuth();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "urls_test_envs":
                    String ift_open = getURLbyNames(URLS.IFT_OPEN);
                    String ift = getURLbyNames(URLS.IFT);
                    String psi = getURLbyNames(URLS.PSI);
                    message = "Открытый ИФТ - " + ift_open + "\nИФТ - " + ift + "\nПСИ - " + psi;
                    break;
                case "accounts_ift":
                    message = "УЗ для ИФТ:\n" + getAccByTypes(ACCOUNTS.IFT) + "\nPass - Qazwsx123!";
                    break;
                case "front_version":
                    message = "Проверить версию фронта можно выполнив в консоли window.initDevTools()\n";
                    break;
                case "accounts_psi":
                    message = "УЗ для ПСИ:\n" + getAccByTypes(ACCOUNTS.PSI) + "\nPass - Qazwsx123!";
                    break;
                case "accounts_kaip_ift":
                    message = "УЗ для КАИП ИФТ:\n" + getAccByTypes(ACCOUNTS.KAIP_IFT) + "\nPass - Qwerty12345!";
                    break;
                case "accounts_kaip_psi":
                    message = "УЗ для КАИП ПСИ:\n" + getAccByTypes(ACCOUNTS.KAIP_PSI) + "\nPass - Qwerty12345!";
                    break;
                case "expired_pass":
                    message = "Сменить пароль можно здесь: \nhttps://vdi.vtb.ru";
                    break;
                case "sched_release":
                    message = "Сфера релизы: \nhttps://sfera.inno.local/pprc";
                    break;
                default:
                    message = "oooh no";
                    break;
            }
            sendMessage(chatId, message);
        }


        // Обработка текстовых команд
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMessageWithButtons(chatId, "Выберите действие:");
            }
        }
    }

    // Метод для отправки сообщения с кнопками
    private void sendMessageWithButtons(long chatId, String text) {
        // Создаем кнопки и клавиатуру
        InlineKeyboardMarkup markup = new BotKeyboards().getKeyboard();

        // Создаем сообщение с клавиатурой
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(markup);

        try {
            execute(message); // Отправляем сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Метод для отправки простого текстового сообщения
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message); // Отправляем сообщение
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getURLbyNames(URLS urls) {
        String message;

        switch (urls.toString()) {
            case "IFT_OPEN":
                message = getFromEnvs.getFromEnvsByName("ift_url_open");
                break;
            case "IFT":
                message = getFromEnvs.getFromEnvsByName("ift_url");
                break;
            case "PSI":
                message = getFromEnvs.getFromEnvsByName("psi_url");
                break;
            default:
                message = "not found";
        }
        return message;
    }

    private String getAccByTypes(ACCOUNTS accounts) {
        String message;

        switch (accounts.toString()) {
            case "KAIP_IFT":
                message = getFromEnvs.getFromEnvsByName("kaip_ift");
                break;
            case "KAIP_PSI":
                message = getFromEnvs.getFromEnvsByName("kaip_psi");
                break;
            case "PSI":
                message = getFromEnvs.getFromEnvsByName("psi");
                break;
            case "IFT":
                message = getFromEnvs.getFromEnvsByName("ift");
                break;
            default:
                message = "not found";
        }
        return message;
    }

    private boolean checkAccess(String userName) {
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("Select * from users");
            System.out.println(resultSet.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true; // to do обрабатывать результаты из resultSet и проверять есть ли там юзер или нету
    }

}


