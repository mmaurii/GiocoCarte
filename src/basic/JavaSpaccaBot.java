package basic;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.List;

public class JavaSpaccaBot extends TelegramLongPollingBot {

    private final static String USERNAME = "Spacca";
    private final static String TOKEN = "6312718703:AAEB92VkQqQbEMamxCX92e73IaR5OeoQfI8";

    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            String text = message.getText();

            if (text.equals("/start")) {
                // Rispondi al comando /start con pulsanti inline
                inviaTastieraInline(chatId);
            } else {
                // Gestisci altri messaggi o comandi
                SendMessage defaultMessage = new SendMessage();
                defaultMessage.setChatId(chatId);
                defaultMessage.setText("Non ho capito il comando. Prova /start");
                try {
                    execute(defaultMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery()) {
            // Gestisci la callback query
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            long chatId = callbackQuery.getMessage().getChatId();

            // Esegui l'azione corrispondente alla callback data
            if (data.equals("comando1")) {
                rispondiAComando1(chatId);
            } else if (data.equals("comando2")) {
                rispondiAComando2(chatId);
            }
        }
    }

    private void inviaTastieraInline(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Benvenuto! Scegli un'opzione:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Crea un pulsante con il callbackData "comando1"
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Classifica");
        button1.setCallbackData("comando1");
        rowInline1.add(button1);

        // Crea un pulsante con il callbackData "comando2"
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Regole");
        button2.setCallbackData("comando2");
        rowInline2.add(button2);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void rispondiAComando1(long chatId) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText("Classifica:");
        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void rispondiAComando2(long chatId) {
        SendMessage responseMessage = new SendMessage();
        responseMessage.setChatId(chatId);
        responseMessage.setText("Regole:");
        try {
            execute(responseMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}