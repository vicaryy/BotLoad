package org.example.service;

import org.example.api_object.User;
import org.example.api_object.message.Message;
import org.example.api_request.*;
import org.example.api_object.Update;
import org.example.controller.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

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

        InputFile videoFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/realshort.mp4"))
                //.fileId("AAMCBAADGQEAAgKGZI8gZ2xNMC25_JxSRL84jlb8ox0AAhoOAAJ1ZIBQs22yjNpUGH4BAAdtAAMvBA")
                .build();

        InputFile thumbnailFile = InputFile.builder()
                .file(new File("/Users/vicary/desktop/logo.jpeg"))
                .build();

        SendVideo sendVideo = SendVideo.builder()
                .chatId(chatId)
                .video(videoFile)
                .build();

        Message message1 = null;
        try {
            message1 = sender.sendRequest(sendVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(message1);

//        GetMe getMe = new GetMe();
//        User user = sender.sendRequest(getMe);
//        System.out.println(user);

    }
}
