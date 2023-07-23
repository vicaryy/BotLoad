package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.example.api_object.bot.bot_command.BotCommand;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_object.message.Message;
import org.example.api_object.other.UserProfilePhotos;
import org.example.api_object.Update;
import org.example.api_request.bot_info.SetMyName;
import org.example.api_request.commands.SetMyCommands;
import org.example.controller.PostController;
import org.example.entity.MessageEntity;
import org.example.repository.MessageRepository;
import org.example.service.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePollingService {
    private final PostController sender;
    private final MessageEntityService messageEntityService;

    public void updateReceiver(Update update) {
        messageEntityService.save(update);
        System.out.println(update);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String chatId = update.getChatId();

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
