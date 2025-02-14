package bot.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotKeyboards {
    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

    public InlineKeyboardMarkup getKeyboard(){
        return markup;
    }

    public BotKeyboards(){
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


        markup.setKeyboard(keyboard);
    }
}
