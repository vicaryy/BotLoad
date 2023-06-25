package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.example.api_object.User;
import org.example.api_object.input_media.InputMediaDocument;
import org.example.api_object.message.Message;
import org.example.api_request.*;
import org.example.api_object.Update;
import org.example.controller.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
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

//        SendMessage sendMessage = SendMessage.builder()
//                .chatId(chatId)
//                .text("*siema*")
//                .build();
//        sendMessage.setParseModeOnMarkdownV2();
//
//        sender.sendRequest(sendMessage);

        InputFile voiceFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/combo_16.ogg"))
                //.fileId("AAMCBAADGQEAAgKGZI8gZ2xNMC25_JxSRL84jlb8ox0AAhoOAAJ1ZIBQs22yjNpUGH4BAAdtAAMvBA")
                .build();

        InputFile thumbnailFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/logo.jpeg"))
                .build();

        List<InputMediaDocument> media = new ArrayList<>();


        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Message message1 = null;
        try {
            message1 = sender.sendRequest(sendVoice);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(gson.toJson(message1));
    }
}
