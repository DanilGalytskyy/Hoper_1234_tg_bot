import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Bot extends TelegramLongPollingBot {

    public static void main(String[] args) throws Exception {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList();
        list.add("квадрат суммы");
        list.add("(a+b)²=a²+2ab+b²");


        FileReader fr = new FileReader("Formula.txt");
        FileWriter fw = new FileWriter("Formula.txt");
        for (int i = 0; i < list.size(); i++) {
            fw.write(list.get(i));
        }
        Scanner sc = new Scanner(fr);
        String s = "";
        fw.write(sc.next());
        fw.close();
        while (sc.hasNextLine()) {
            s = sc.nextLine();
        }

    }

    // public Message msg;
    //  public String txt = msg.getText();

    public void onUpdateReceived(Update update) {
        //Message msg = update.getMessage();
        //String txt = msg.getText();


        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                Message msg = update.getMessage();
                String txt = msg.getText();
                long chatId = msg.getChatId();
                if (txt.equals("/start")) {
                    sndMessage(chatId, " Привет, выбери предмет: ");
                } else {
                    sndInline(chatId);
                }
            }
        } else if (update.hasCallbackQuery()) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (update.getCallbackQuery().getData().equals("Button \"Вибір 1\" has been pressed"))
                sndMessage(chatId, "(a+b)²=a²+2ab+b²");
            if (update.getCallbackQuery().getData().equals("Button \"Вибір 2\" has been pressed"))
                sndMessage(chatId, "(a-b)²=a²-2ab+b²");
            if (update.getCallbackQuery().getData().equals("Call 1.2"))
                sndMessage(chatId, "ax²±bx±c=0 D=b²-4ac");
            if (update.getCallbackQuery().getData().equals("Button \"Вибір 3\" has been pressed"))
                sndMessage(chatId, "x²±bx±c=0; x1+x2=-b; x1*x2=c");
            if (update.getCallbackQuery().getData().equals("Button \"Вибір 4\" has been pressed"))
                sndMessage(chatId, "a²-b²=(a-b)*(a+b)");
            sndInline(chatId);
        }
    }


    public void sndInline(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton3 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton4 = new InlineKeyboardButton();

        inlineKeyboardButton1.setText("квадрат сумы");
        inlineKeyboardButton1.setCallbackData("Button \"Вибір 1\" has been pressed");
        inlineKeyboardButton2.setText("квадрат разницы двух выражений");
        inlineKeyboardButton2.setCallbackData("Button \"Вибір 2\" has been pressed");
        inlineKeyboardButton3.setText("теорема Виета");
        inlineKeyboardButton3.setCallbackData("Button \"Вибір 3\" has been pressed");
        inlineKeyboardButton4.setText("разности квадратов");
        inlineKeyboardButton4.setCallbackData("Button \"Вибір 4\" has been pressed");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow4 = new ArrayList<>();

        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("дискриминант").setCallbackData("Call 1.2"));
        keyboardButtonsRow2.add(inlineKeyboardButton2);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Выбери формулу:");
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public String getBotUsername() {
        return "Formula";
    }

    public String getBotToken() {
        return "660268159:AAFKx62UZTefO6TYkBzr4ZZHs5UZjL7xuuM";
    }


    List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();


    public void setKeyboardRowList() {
        keyboardRowList.clear();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow keyboardFirstRow1 = new KeyboardRow();
        keyboardFirstRow1.add("Геометрия");
        keyboardFirstRow1.add("Алгебра");

        keyboardRowList.add(keyboardFirstRow1);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }


    private void sndMessage(long chatId, String text) {
        SendMessage sm = new SendMessage();
        //sm.enableMarkdown(true);
        sm.setChatId(chatId);
        sm.setText(text);
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}