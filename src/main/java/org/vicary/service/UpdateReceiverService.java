package org.vicary.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.vicary.api_object.Update;
import org.vicary.api_object.User;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.entity.ActiveRequestEntity;
import org.vicary.entity.UserEntity;
import org.vicary.exception.DownloadedFileNotFoundException;
import org.vicary.exception.InvalidBotRequestException;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.pattern.Pattern;
import org.vicary.service.downloader.*;
import org.vicary.service.file_service.*;
import org.vicary.service.mapper.MessageMapper;
import org.vicary.service.response.*;
import org.vicary.service.quick_sender.QuickSender;
import org.vicary.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UpdateReceiverService {
    private static final Logger logger = LoggerFactory.getLogger(UpdateReceiverService.class);

    private final QuickSender quickSender;

    private final MessageEntityService messageEntityService;

    private final UserMapper userMapper;

    private final ActiveRequestService activeRequestService;

    private final UserService userService;

    private final AdminResponse adminResponse;

    private final MessageMapper messageMapper;

    private final ResponseInfo info;

    private final YouTubeDownloader youtubeDownloader;

    private final TwitterDownloader twitterDownloader;

    private final TikTokDownloader tiktokDownloader;

    private final InstagramDownloader instagramDownloader;

    private final InstagramFileService instagramFileService;

    private final TikTokFileService tiktokFileService;

    private final TwitterFileService twitterFileService;

    private final YouTubeFileService youtubeFileService;

    private final LinkResponse linkResponse;

    private final Pattern pattern;

    public void updateReceiver(Update update) {
        if (update.getMessage() == null) {
            logger.warn("Got update without Message object.");
            throw new InvalidBotRequestException(null, null);
        }

        User user = update.getMessage().getFrom();
        String text = update.getMessage().getText().trim();
        String userId = user.getId().toString();
        String chatId = update.getChatId();


        // SAVING MESSAGE TO REPOSITORY
        messageEntityService.save(messageMapper.map(update));
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

            String URL = getURL(text);
            Downloader downloader = null;
            FileService fileService = null;
            FileRequest fileRequest = null;
            try {
                if (pattern.isYouTubeURL(URL)) {
                    downloader = youtubeDownloader;
                    fileService = youtubeFileService;
                } else if (pattern.isTwitterURL(URL)) {
                    downloader = twitterDownloader;
                    fileService = twitterFileService;
                } else if (pattern.isTikTokURL(URL)) {
                    downloader = tiktokDownloader;
                    fileService = tiktokFileService;
                } else if (pattern.isInstagramURL(URL)) {
                    downloader = instagramDownloader;
                    fileService = instagramFileService;
                }

                if (downloader != null) {
                    Message botMessageInfo = quickSender.messageWithReturn(chatId, info.getGotTheLink() + info.getHoldOn(), true);
                    quickSender.chatAction(chatId, "typing");

                    fileRequest = getFileRequest(update, downloader, botMessageInfo.getMessageId());

                    linkResponse.sendFile(fileRequest, downloader, fileService);
                }

            } catch (WebClientResponseException ex) {
                logger.warn("---------------------------");
                logger.warn("Status code: " + ex.getStatusCode());
                logger.warn("Description: " + ex.getStatusText());
                logger.warn("---------------------------");
            } catch (DownloadedFileNotFoundException | InvalidBotRequestException ex) {
                logger.warn(ex.getLoggerMessage());
                quickSender.editMessageText(fileRequest.getEditMessageText(), ex.getMessage());
            } catch (WebClientRequestException | NoSuchElementException | IOException ex) {
                logger.warn("Expected exception: ", ex);
            } catch (Exception ex) {
                logger.warn("Unexpected exception: ", ex);
                quickSender.editMessageText(fileRequest.getEditMessageText(), "Sorry, but something goes wrong.");
            } finally {
                // DELETE USER FROM ACTIVE REQUESTS
                activeRequestService.deleteById(request.getId());
            }


            // ADMIN STUFF
            if (userService.isUserAdmin(userId))
                adminResponse.response(text, chatId);
        }
    }

    public FileRequest getFileRequest(Update update, Downloader downloader, int messageId) {
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final boolean premium = userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);
        final EditMessageText editMessageText = EditMessageText.builder()
                .chatId(update.getChatId())
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        return FileRequest.builder()
                .URL(getURL(text))
                .chatId(update.getChatId())
                .extension(getExtension(text, downloader.getAvailableExtensions()))
                .multiVideoNumber(getMultiVideoNumber(text))
                .premium(premium)
                .editMessageText(editMessageText)
                .build();
    }

    public int getMultiVideoNumber(String text) {
        int number = 0;
        try {
            number = Arrays.stream(text.split(" "))
                    .limit(3)
                    .skip(1)
                    .filter(s -> s.startsWith("#"))
                    .findFirst()
                    .map(e -> e.substring(1))
                    .map(Integer::parseInt)
                    .get();
        } catch (NumberFormatException ex) {
            throw new InvalidBotRequestException(
                    info.getWrongMultiVideoNumber(),
                    "User specify wrong multi-video number.");
        } catch (NoSuchElementException ignored) {
        }
        return Math.max(number, 0);
    }

    public String getExtension(String text, List<String> availableExtensions) {
        String[] textArray = text.toLowerCase().trim().split(" ");
        if (textArray.length > 1 && !textArray[1].startsWith("#")) {
            if (availableExtensions.contains(textArray[1]))
                return textArray[1];
            else
                throw new InvalidBotRequestException(
                        info.getWrongExtension(),
                        String.format("User specify wrong extension '%s'.", textArray[1]));
        } else if (textArray.length > 2 && !textArray[2].startsWith("#")) {
            if (availableExtensions.contains(textArray[2]))
                return textArray[2];
        }
        return availableExtensions.stream().findFirst().get();
    }

    public String getURL(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}
