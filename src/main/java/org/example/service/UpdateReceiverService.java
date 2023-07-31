package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_object.User;
import org.example.api_request.InputFile;
import org.example.api_request.send.SendAudio;
import org.example.api_request.send.SendDocument;
import org.example.api_request.send.SendMessage;
import org.example.pattern.YoutubePattern;
import org.example.repository.UserRepository;
import org.example.service.bot_response.TextResponse;
import org.example.service.bot_response.YouTubeResponse;
import org.example.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class UpdateReceiverService {
    private final MessageEntityService messageEntityService;
    private final TextResponse textResponse;
    private final YouTubeResponse youtubeResponse;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RequestService requestService;

    public void updateReceiver(Update update) {
        messageEntityService.save(update);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(update));
        String text = "";
        User user = update.getMessage().getFrom();

        if (update.getMessage().getText() != null)
            text = update.getMessage().getText();

//        String fileN = "Axwell /\\\\ Ingrosso.mp3";
//        SendAudio sendAudio = SendAudio.builder()
//                .chatId(update.getChatId())
//                .audio(InputFile.builder()
//                        .file(new File("/Users/vicary/desktop/" + fileN))
//                        .build())
//                .build();
//
//        try {
//            requestService.sendRequest(sendAudio);
//        } catch (Exception e) {
//        }

        if (userRepository.findByUserId(user.getId().toString()) == null)
            userRepository.save(userMapper.map(user));


        if (YoutubePattern.checkUrlValidation(text))
            youtubeResponse.response(update);

//            if (!text.startsWith("/"))
//                textResponse.response(update);
    }
}
