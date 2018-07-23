package com.skobelev.controller;

import net.aksingh.owmjapis.api.APIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.skobelev.sercvice.UserService;


@Component
public class YotaBot extends TelegramLongPollingBot {


    @Autowired
    private UserService userService;

    private boolean setCity = false;
    private boolean setHours = false;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {

            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String text = message.getText();
            SendMessage response = new SendMessage();
            response.setChatId(chatId);

            if (setHours) {

                try {
                    response.setText(userService.getWeatherDayUser(chatId, Integer.parseInt(text)));
                } catch (APIException ex) {
                    System.err.println(ex);
                }

                setHours = false;

            } else if (setCity) {

                userService.userSetCity(chatId, text);
                setCity = false;

                StringBuilder sb = new StringBuilder();

                sb.append("ok. At now your city is " + text);
                sb.append(System.lineSeparator());

                response.setText(sb.toString());

            } else if (text.startsWith("/later")) {

                StringBuilder sb = new StringBuilder();
                sb.append("Please write time ");

                response.setText(sb.toString());

                setHours = true;

            } else if (text.startsWith("/now")) {
                try {
                    response.setText(userService.getWeather(chatId));
                } catch (APIException ex) {
                    System.err.println(ex);
                }

            } else if (text.startsWith("/day")) {
                try {
                    response.setText(userService.getWeatherDay(chatId));
                } catch (APIException ex) {
                    System.err.println(ex);
                }
            } else if (text.startsWith("/setCity")) {

                StringBuilder sb = new StringBuilder();
                sb.append("Please write yout city");

                response.setText(sb.toString());

                setCity = true;

            } else if (text.startsWith("/statusAll")) {

                response.setText(userService.printUserStatusAll());

            } else if (text.startsWith("/status")) {

                response.setText(userService.printUserStatus(chatId));

            } else if (text.startsWith("/help")) {

                response.setText(userService.printHelp());

            } else if (text.startsWith("/start")) {

                userService.createUser(message.getChatId());
                response.setText(userService.printGreeting());

            } else {
                response.setText("I dont know this command!");
            }


            executeResponse(response);
        }


    }

    private void executeResponse(SendMessage response) {
        try {
            execute(response);
        } catch (TelegramApiException e) {
            System.err.println(e);
        }
    }

}
