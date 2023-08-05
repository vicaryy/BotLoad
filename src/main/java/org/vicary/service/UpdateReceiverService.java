package org.vicary.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendMessage;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.pattern.YoutubePattern;
import org.vicary.repository.ActiveRequestRepository;
import org.vicary.repository.UserRepository;
import org.vicary.service.bot_response.TextResponse;
import org.vicary.service.bot_response.YouTubeResponse;
import org.vicary.service.mapper.UserMapper;
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
    private final ActiveRequestRepository activeRequestRepository;
    private final RequestService requestService;
    private final UserService userService;

    public void updateReceiver(Update update) {
        messageEntityService.save(update);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(update));
        String text = "";
        User user = update.getMessage().getFrom();
        String userId = user.getId().toString();

        if (update.getMessage().getText() != null)
            text = update.getMessage().getText();


        if (!userService.existsByUserId(user.getId().toString()))
            userService.saveUser(userMapper.map(user));

        if (userId.equals("1935527130") && text.contains("/set premium")) {
            String[] premiums = text.split(" ");
            if (premiums.length > 1)
                userService.updateUserToPremiumByNick(premiums[2]);
        }

        if (userId.equals("1935527130") && text.contains("/set standard")) {
            String[] premiums = text.split(" ");
            if (premiums.length > 1)
                userService.updateUserToStandardByNick(premiums[2]);
        }

//        InputFile inputFile = new InputFile();
//        inputFile.setFile(new File("/Users/vicary/desktop/where are ü now (slowed + reverb) - skrillex, diplo ft Just.mp3"));
//        SendAudio sendAudio = SendAudio.builder()
//                .chatId(update.getChatId())
//                .audio(inputFile)
//                .build();
//
//        try {
//            System.out.println("Wysylam?");
//            requestService.sendRequest(sendAudio);
//        } catch (Exception e) {
//        }
        if (!activeRequestRepository.existsByUserId(userId)) {
            ActiveRequestEntity activeRequestEntity = ActiveRequestEntity.builder()
                    .userId(userId)
                    .build();
            activeRequestEntity = activeRequestRepository.save(activeRequestEntity);

            try {
                if (YoutubePattern.checkUrlValidation(text))
                    youtubeResponse.response(update);
            } catch (Exception e) {
                e.printStackTrace();
            }

            activeRequestRepository.deleteById(activeRequestEntity.getId());
        }
    }
}
