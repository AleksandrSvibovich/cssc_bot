package bot;

import bot.bd.CreateTablesBD;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Tgbot {
    public static void main(String[] args) {
        TelegramBotsApi botsApi;

        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot());
            new CreateTablesBD().createTables();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

