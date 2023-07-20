package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.api_object.bot.bot_command.BotCommand;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_object.message.Message;
import org.example.api_object.other.UserProfilePhotos;
import org.example.api_object.Update;
import org.example.api_request.bot_info.SetMyName;
import org.example.api_request.chat.chat_invite.CreateChatInviteLink;
import org.example.api_request.commands.SetMyCommands;
import org.example.controller.PostController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        System.out.println(update);
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

        List<BotCommand> botCommandList = new ArrayList<>();
        BotCommand botCommand1 = new BotCommand("/Dupa", "xddd");
        botCommandList.add(botCommand1);

        SetMyCommands setMyCommands = new SetMyCommands(botCommandList);
        setMyCommands.setScopeOnDefault();

        SetMyName setMyName = new SetMyName();
        setMyName.setName("test bot - vicary");


        Message message1 = null;
        Boolean b = null;
        UserProfilePhotos userProfilePhotos = null;
        String s = null;
        ChatInviteLink chatInviteLink = null;
        try {
//             b = sender.sendRequest(setMyCommands);
             //sender.sendRequest(setMyName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(gson.toJson(update));
//        System.out.println(gson.toJson(chatInviteLink));
    }
}
