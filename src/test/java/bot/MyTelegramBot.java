package bot;

import bot.enums.ACCOUNTS;
import bot.enums.URLS;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import checks.auxiliary.ApiClient;
import checks.auxiliary.ApiConfig;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MyTelegramBot extends TelegramLongPollingBot {
    Properties properties = new Properties();
    FileInputStream input = null;
    public static final String LOGIN_BODY = "src/test/java/checks/jsons/loginBody.json";

    @Override
    public String getBotUsername() {
        return "cssc_helper_bot"; // Укажите имя вашего бота
    }

    @Override
    public String getBotToken() {

        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("bot"); // Укажите токен вашего бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Обработка нажатий на кнопки
        String message;
        if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callData) {
                case "status_ift":
                    try {
                        sendMessage(chatId, "Проверяю");
                        message = checkAuth();
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
                    message = getAccByTypes(ACCOUNTS.IFT) + "\nPass - Qazwsx123!";
                    break;
                case "accounts_psi":
                    message = getAccByTypes(ACCOUNTS.PSI) + "\nPass - Qazwsx123!";
                    break;
                case "accounts_kaip_ift":
                    message = getAccByTypes(ACCOUNTS.KAIP_IFT) + "\nPass - Qwerty12345!";
                    break;
                case "accounts_kaip_psi":
                    message = getAccByTypes(ACCOUNTS.KAIP_PSI) + "\nPass - Qwerty12345!";
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

    public String checkAuth() throws Exception {
        Playwright pw = Playwright.create();
        Browser chromium = pw.chromium().launch();
        Page page = chromium.newPage();
        ApiClient apiClient = new ApiClient(page);
        String message;
        String bad_message = "Authorization - doesn't work";
        String good_message = "Authorization - OK";

        String jsonBody = apiClient.loadJsonFromFile(LOGIN_BODY);
        APIResponse apiResponsee = apiClient.sendPostRequest(ApiConfig.URL_LOGIN, jsonBody);
        if (apiResponsee.status()!=200){
            message = bad_message;
            return message;
        }
        // ТО ДУ Додедалть проверки на каждый запрос!!

        String jsonClient = apiClient.convertByte2String(apiClient.sendGetRequest(ApiConfig.URL_CLIENTS));
        JSONObject jsonObject = new JSONObject(jsonClient);
        String mdmCode = jsonObject.getJSONObject("result") // получаем MDM код организации из структуры JSON
                .getJSONObject("data")
                .getJSONObject("multiClient")
                .getJSONArray("all")
                .getJSONObject(0)
                .getString("mdmCode");

        apiResponsee = apiClient.sendPostRequest(ApiConfig.URL_SET_ACTIVE_CLIENT,
                "{\"mdmCode\":\"" + mdmCode + "\"}");
        if (apiResponsee.status()!=200){
            message = bad_message;
            return message;
        }

        APIResponse getUserInfo = apiClient.sendGetRequest(ApiConfig.URL_GET_USER_INFO);
        if (getUserInfo.status() == 200) {
            return good_message;
        } else {
            return bad_message;
        }
    }


    // Метод для отправки сообщения с кнопками
    private void sendMessageWithButtons(long chatId, String text) {
        // Создаем кнопки
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("ИФТ статус");
        button1.setCallbackData("status_ift");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Адреса тест стендов");
        button2.setCallbackData("urls_test_envs");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Учетки для ИФТ");
        button3.setCallbackData("accounts_ift");

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Учетки для ПСИ");
        button4.setCallbackData("accounts_psi");

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Учётки КАиП для ИФТ");
        button5.setCallbackData("accounts_kaip_ift");

        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("Учётки КАиП для ПСИ");
        button6.setCallbackData("accounts_kaip_psi");


        // Создаем ряд кнопок
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button1);
        row.add(button2);


        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(button3);
        row2.add(button4);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(button5);
        row3.add(button6);

        // Создаем клавиатуру
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(row);
        keyboard.add(row2);
        keyboard.add(row3);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);

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

//    private void getAccsKAIP(long chatId) {
//        String message = getAccByTypes(ACCOUNTS.KAIP);
//        sendMessage(chatId, message);
//    }


    private String getURLbyNames(URLS urls) {
        String message;
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        switch (urls.toString()) {
            case "IFT_OPEN":
                message = properties.getProperty("ift_url_open");
                break;
            case "IFT":
                message = properties.getProperty("ift_url");
                break;
            case "PSI":
                message = properties.getProperty("psi_url");
                break;
            default:
                message = "not found";
        }
        return message;
    }

    private String getAccByTypes(ACCOUNTS accounts) {
        String message;
        try {
            input = new FileInputStream("config.properties");
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        switch (accounts.toString()) {
            case "KAIP_IFT":
                message = properties.getProperty("kaip_ift");
                break;
            case "KAIP_PSI":
                message = properties.getProperty("kaip_psi");
                break;
            case "PSI":
                message = properties.getProperty("psi");
                break;
            case "IFT":
                message = properties.getProperty("ift");
                break;
            default:
                message = "not found";
        }
        return message;
    }


}


