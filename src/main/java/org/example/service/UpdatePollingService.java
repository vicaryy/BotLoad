package org.example.service;

import org.example.api_object.User;
import org.example.api_request.*;
import org.example.api_object.Update;
import org.example.controller.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UpdatePollingService {
    private final PostController sender;

    @Autowired
    public UpdatePollingService(PostController sender) {
        this.sender = sender;
    }

    public void updateReceiver(Update update) {
        String chatId = update.getChatId();

//        System.out.println(update);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text("*siema*")
                .build();
        sendMessage.setParseModeOnMarkdownV2();


        GetMe getMe = new GetMe();
        User user = sender.sendRequest(getMe);
        System.out.println(user);

    }
}
