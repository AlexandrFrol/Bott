package org.example;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "@Botstertosterbot";
    }

    @Override
    public String getBotToken() {
        return "7051547761:AAFfxML505BV1SgJQJiV4IC4gJpwoTk82lI";
    }

    Integer answersCorrect = 0;
    Integer answersAnswered = 0;
    String task = "-";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update.getMessage(), update);
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handleMessage(Message message, Update update) {
        String chatId = message.getChatId().toString();
        String response = "";

        if ( (message.getText().equals("/start_test") || message.getText().equals("/start")) && answersAnswered == 0) {
            response = "Start?";
            sendInlineKeyboard(chatId, response, "Start", "start");
        } else {
            if (task.equals("1")) {
                String input = message.getText();
                String[] words = input.split(" ");

                if (words.length >= 4) {
                    String mail = words[0];
                    String name = words[1] + " " + words[2] + " " + words[3];
                    System.out.println(mail);
                    System.out.println(name);


                    User user = new User(chatId, mail, name);
                    System.out.println(user.toString());

                    StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                            .configure("hibernate.cfg.xml").build();
                    Metadata metadata = new MetadataSources(standardServiceRegistry)
                            .getMetadataBuilder()
                            .build();
                    SessionFactory sessionFactory = metadata.getSessionFactoryBuilder()
                            .build();
                    Session session = sessionFactory.openSession();
                    Transaction transaction = session.beginTransaction();

                    session.save(user);

                    transaction.commit();
                    sessionFactory.close();

                    sendMessage(chatId, "Got your info");
                    sendMessage("5075636674", "One: @" + update.getMessage().getFrom().getUserName());
                } else {
                    response = "Not enough info";
                    sendMessage(chatId, response);
                    return;
                }
            }
            sendMessage(chatId, response);
        }

    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String response = "";

        if (callbackData.equals("start") && answersAnswered == 0 && task != "1"){
            question1(chatId);
        } else if (callbackData.contains("a") && answersAnswered == 0){
            answersAnswered++;
            if (callbackData.equals("a1")){
                answersCorrect++;
            }
            question2(chatId);
        }else if (callbackData.contains("b") && answersAnswered == 1){
            answersAnswered++;
            if (callbackData.equals("b3")){
                answersCorrect++;
            }
            question3(chatId);
        }else if (callbackData.contains("c") && answersAnswered == 2){
            answersAnswered++;
            if (callbackData.equals("c3")){
                answersCorrect++;
            }
            question4(chatId);
        }else if (callbackData.contains("d") && answersAnswered == 3){
            answersAnswered++;
            if (callbackData.equals("d4")){
                answersCorrect++;
            }
            response = "You answered right for: " + answersCorrect*100/answersAnswered + "%";
            sendMessage(chatId, response);
            if (answersCorrect*100/answersAnswered>70){
                response = "You fit for this position. Now send us your email and full name in this format: `Example@Email.com, Name SecondName Surname`.";
                task = "1";
            } else{
                response = "You don't fit for this position.";
            }
            sendMessage(chatId, response);

        }
    }

    private void sendInlineKeyboard(String chatId, String text, String buttonText, String callbackData) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(buttonText);
        button1.setCallbackData(callbackData);
        row1.add(button1);

        keyboard.add(row1);
        inlineKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void question1(String chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String response = "Whats the difference in string stringbuilder?";

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button1.setText("No difference");
        button1.setCallbackData("a1");
        button2.setText("Data type difference");
        button2.setCallbackData("a2");
        button3.setText("Difference in copies");
        button3.setCallbackData("a3");
        button4.setText("Class and primitive type");
        button4.setCallbackData("a4");
        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void question2(String chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String response = "Choose primitive type";

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button1.setText("int double");
        button1.setCallbackData("b1");
        button2.setText("bool integer");
        button2.setCallbackData("b2");
        button3.setText("short byte");
        button3.setCallbackData("b3");
        button4.setText("boolean float");
        button4.setCallbackData("b4");
        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void question3(String chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String response = "Why are classes LocalDate and LocalDateTime better then class Date?";

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button1.setText("Convenient");
        button1.setCallbackData("c1");
        button2.setText("Precise");
        button2.setCallbackData("c2");
        button3.setText("Converts to any format");
        button3.setCallbackData("c3");

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void question4(String chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String response = "What keyword declares that a class inherits another class?";

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button1.setText("Expand");
        button1.setCallbackData("d1");
        button2.setText("Extend");
        button2.setCallbackData("d2");
        button3.setText("Extended");
        button3.setCallbackData("d3");
        button4.setText("Extends");
        button4.setCallbackData("d4");
        row1.add(button1);
        row2.add(button2);
        row3.add(button3);
        row4.add(button4);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        inlineKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(response);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        try {
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}