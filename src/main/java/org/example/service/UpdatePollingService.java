package org.example.service;

import org.example.api_object.input_media.InputMediaPhoto;
import org.example.api_request.InputFile;
import org.example.api_request.SendAnimation;
import org.example.api_request.SendAudio;
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

        InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();


        //SendMessage sendMessage = SendMessage.builder().chatId("123").build();


//
//        SendMessage message1 = SendMessage.builder()
//                .chatId(chatId)
//                .text(update.getMessage().getText())
//                .build();
//        System.out.println(update.getMessage().getChat().getId());
//
//        SendMessage message2 = SendMessage.builder()
//                .chatId(chatId)
//                .text("spadaka")
//                .build();

        File someAudio = new File("/Users/vicary/desktop/test.mp3");
        SendAudio audio = SendAudio.builder()
                .chatId(chatId)
                .audio(new InputFile("MUZYKA", someAudio))
                .build();

        SendAnimation animation = new SendAnimation(chatId, new InputFile("animation", new File("/Users/vicary/desktop/nailsing.gif")));

        //sender.execute(message1);
        sender.execute(audio);
    }
}
