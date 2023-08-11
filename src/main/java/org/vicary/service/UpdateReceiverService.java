package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.service.bot_response.AdminResponse;
import org.vicary.service.youtube.YoutubePattern;
import org.vicary.service.bot_response.YouTubeResponse;
import org.vicary.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UpdateReceiverService {

    private final MessageEntityService messageEntityService;

    private final YouTubeResponse youtubeResponse;

    private final UserMapper userMapper;

    private final ActiveRequestService activeRequestService;

    private final UserService userService;

    private final AdminResponse adminResponse;

    public void updateReceiver(Update update) {
        User user = update.getMessage().getFrom();
        String text = update.getMessage().getText();
        String userId = user.getId().toString();


        // SAVING MESSAGE TO REPOSITORY
        messageEntityService.save(update);

        // ADDING NEW USER TO USER REPOSITORY
        if (!userService.existsByUserId(user.getId().toString()))
            userService.saveUser(userMapper.map(user));

        // CHECKING IF USER IS NOT ALREADY IN REQUESTS REPO
        if (!activeRequestService.existsByUserId(userId)) {
            // ADDING USER TO ACTIVE REQUESTS REPO
            var request = activeRequestService.saveActiveUser(new ActiveRequestEntity(userId));

            try {
                String url = Arrays.stream(text.trim().split(" ")).findFirst().orElse("");
                if (YoutubePattern.checkUrlValidation(url))
                    youtubeResponse.response(update);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // DELETE USER FROM ACTIVE REQUESTS
            activeRequestService.deleteById(request.getId());
        }


        // ADMIN STUFF
        if (userId.equals("1935527130"))
            adminResponse.response(update);
    }
}
