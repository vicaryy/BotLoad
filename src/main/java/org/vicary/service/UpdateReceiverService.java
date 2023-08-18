package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.pattern.twitter.TwitterPattern;
import org.vicary.service.bot_response.AdminResponse;
import org.vicary.service.bot_response.TwitterResponse;
import org.vicary.service.quick_sender.QuickSender;
import org.vicary.pattern.youtube.YoutubePattern;
import org.vicary.service.bot_response.YouTubeResponse;
import org.vicary.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UpdateReceiverService {
    private static final Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final QuickSender quickSender;

    private final MessageEntityService messageEntityService;;

    private final UserMapper userMapper;

    private final ActiveRequestService activeRequestService;

    private final UserService userService;

    private final AdminResponse adminResponse;

    private final YouTubeResponse youtubeResponse;

    private final TwitterResponse twitterResponse;

    public void updateReceiver(Update update) {
        User user = update.getMessage().getFrom();
        String text = update.getMessage().getText().trim();
        String userId = user.getId().toString();
        String chatId = update.getChatId();


        // SAVING MESSAGE TO REPOSITORY
        messageEntityService.save(update);
        logger.info("Got message from user id '{}'", userId);

        // ADDING NEW USER TO USER REPOSITORY
        if (!userService.existsByUserId(user.getId().toString())) {
            userService.saveUser(userMapper.map(user));
            logger.info("New user with id '{}' saved to repository.", userId);
        }

        // CHECKING IF USER IS NOT ALREADY IN REQUESTS REPO
        if (!activeRequestService.existsByUserId(userId)) {
            // ADDING USER TO ACTIVE REQUESTS REPO
            var request = activeRequestService.saveActiveUser(new ActiveRequestEntity(userId));

            try {
                String url = Arrays.stream(text.trim().split(" ")).findFirst().orElse("");

                if (YoutubePattern.checkUrlValidation(url))
                    youtubeResponse.response(update);
                else if (TwitterPattern.checkUrlValidation(url))
                    twitterResponse.response(update);

            } catch (WebClientResponseException ex) {
                logger.warn("---------------------------");
                logger.warn("Status code: " + ex.getStatusCode());
                logger.warn("Description: " + ex.getStatusText());
                logger.warn("---------------------------");
            } catch (WebClientRequestException | IllegalArgumentException | NoSuchElementException | IOException ex) {
                logger.warn("Expected exception: ", ex);
            } catch (Exception ex) {
                logger.warn("Unexpected exception: ", ex);
                quickSender.message(chatId, "Sorry, but something goes wrong.", false);
            } finally {
                // DELETE USER FROM ACTIVE REQUESTS
                activeRequestService.deleteById(request.getId());
            }


            // ADMIN STUFF
            if (userService.isUserAdmin(userId))
                adminResponse.response(update);
        }
    }
}
