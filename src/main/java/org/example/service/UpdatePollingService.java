package org.example.service;

import org.example.api_object.User;
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

        System.out.println(update);

        //SendMessage sendMessage = SendMessage.builder().chatId("123").build();



        SendMessage message1 = SendMessage.builder()
                .chatId(chatId)
                .text(update.getMessage().getText())
                .build();
//
//        SendMessage message2 = SendMessage.builder()
//                .chatId(chatId)
//                .text("spadaka")
//                .build();

//        File someAudio = new File("/Users/vicary/desktop/test.mp3");
//        SendAudio audio = SendAudio.builder()
//                .chatId(chatId)
//                .audio(new InputFile("MUZYKA", someAudio))
//                .build();

        //SendAnimation animation = new SendAnimation(chatId, new InputFile("animation", new File("/Users/vicary/desktop/nailsing.gif")));

        sender.execute(message1);
        GetMe getMe = new GetMe();
        InputFile inputFile = sender.executeMethod(getMe);
        System.out.println(inputFile);
        //sender.execute(audio);
    }
}
