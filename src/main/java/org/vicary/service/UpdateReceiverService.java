package org.vicary.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.api_request.send.SendMessage;
import org.vicary.pattern.YoutubePattern;
import org.vicary.repository.UserRepository;
import org.vicary.service.bot_response.TextResponse;
import org.vicary.service.bot_response.YouTubeResponse;
import org.vicary.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

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


        if (userRepository.findByUserId(user.getId().toString()) == null)
            userRepository.save(userMapper.map(user));

        try {
            if (YoutubePattern.checkUrlValidation(text))
                youtubeResponse.response(update);
        } catch (Exception e) {
            e.printStackTrace();
        }

//            if (!text.startsWith("/"))
//                textResponse.response(update);
    }
}
