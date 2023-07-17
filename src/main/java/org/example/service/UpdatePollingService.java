package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_object.message.Message;
import org.example.api_object.other.UserProfilePhotos;
import org.example.api_object.Update;
import org.example.api_request.chat.chat_invite.CreateChatInviteLink;
import org.example.controller.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdatePollingService {
    private final PostController sender;

    @Autowired
    public UpdatePollingService(PostController sender) {
        this.sender = sender;
    }

    public void updateReceiver(Update update) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String chatId = update.getChatId();


//        System.out.println(update);

//        SendMessage sendMessage = SendMessage.builder()
//                .chatId(chatId)
//                .text("*siema*")
//                .build();
//        sendMessage.setParseModeOnMarkdownV2();
//
//        sender.sendRequest(sendMessage);


        CreateChatInviteLink createChatInviteLink = new CreateChatInviteLink(chatId);


        Message message1 = null;
        Boolean b = null;
        UserProfilePhotos userProfilePhotos = null;
        String s = null;
        ChatInviteLink chatInviteLink = null;
        try {
             chatInviteLink = sender.sendRequest(createChatInviteLink);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(gson.toJson(message1));
        System.out.println(gson.toJson(chatInviteLink));
    }
}
