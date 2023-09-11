package org.vicary.service;

import com.mpatric.mp3agic.ID3v1Genres;
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
import org.vicary.model.ID3TagData;
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
            return;
        }

        User user = update.getMessage().getFrom();
        String text = "";
        String userId = user.getId().toString();
        String chatId = update.getChatId();

        if (update.getMessage().getText() != null)
            text = update.getMessage().getText().trim();

        // SAVING MESSAGE TO REPOSITORY
        messageEntityService.save(messageMapper.map(update));
        logger.info("Got message from user id '{}'", userId);

        // ADDING NEW USER TO USER REPOSITORY
        if (!userService.existsByUserId(user.getId().toString())) {
            userService.saveUser(userMapper.map(user));
            logger.info("New user with id '{}' saved to repository.", userId);
        }

        // CHECKING IF USER IS NOT ALREADY IN REQUESTS REPO
        if (activeRequestService.existsByUserId(userId)) {
            quickSender.message(chatId, info.getOneRequestAtTime(), true);
            return;
        }

        // ADDING USER TO ACTIVE REQUESTS REPO
        var request = activeRequestService.saveActiveUser(new ActiveRequestEntity(userId));

        String URL = getURL(text);
        Downloader downloader = null;
        FileService fileService = null;
        FileRequest fileRequest = null;
        EditMessageText editMessageText = null;

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

                editMessageText = getEditMessageText(chatId, botMessageInfo.getMessageId());
                fileRequest = getFileRequest(update, downloader, editMessageText);


                linkResponse.sendFile(fileRequest, downloader, fileService);
            }

        } catch (WebClientResponseException ex) {
            logger.warn("---------------------------");
            logger.warn("Status code: " + ex.getStatusCode());
            logger.warn("Description: " + ex.getStatusText());
            logger.warn("---------------------------");
        } catch (DownloadedFileNotFoundException | InvalidBotRequestException ex) {
            logger.warn(ex.getLoggerMessage());
            quickSender.editMessageText(editMessageText, ex.getMessage());
        } catch (WebClientRequestException | NoSuchElementException | IllegalArgumentException |
                 IOException ex) {
            logger.warn("Expected exception: ", ex);
            quickSender.editMessageText(editMessageText, info.getError());
        } catch (Exception ex) {
            logger.warn("Unexpected exception: ", ex);
            quickSender.editMessageText(editMessageText, info.getError());
        } finally {
            // DELETE USER FROM ACTIVE REQUESTS
            activeRequestService.deleteById(request.getId());
        }


        // ADMIN STUFF
        if (userService.isUserAdmin(userId))
            adminResponse.response(text, chatId);
    }

    public FileRequest getFileRequest(Update update, Downloader downloader, EditMessageText editMessageText) {
        String text = update.getMessage().getText();
        String userId = update.getMessage().getFrom().getId().toString();
        String extension = downloader.getAvailableExtensions().stream().findFirst().get();
        String URL = getURL(text);
        int multiVideoNumber = 0;
        ID3TagData id3TagData = null;
        boolean premium = getPremium(userId);

        text = removeUrl(text);
        text = replaceDashesTo11DASH11(text);

        String[] arrayText = text.split("-");

        for (String s : arrayText) {
            if (s.startsWith("ext"))
                extension = getExtension(s.substring(1).trim().toLowerCase(), downloader.getAvailableExtensions());
            else if (s.startsWith("mul"))
                multiVideoNumber = getMultiVideoNumber(s.substring(1).trim());
            else if (s.startsWith("tag"))
                id3TagData = getId3Tag(s.substring(3).trim());
        }


        if (!extension.equals("mp3") && id3TagData != null)
            throw new InvalidBotRequestException(info.getId3tagOnlyWithMp3(), "[ID3Tag] User tried to add ID3Tag for '" + extension + "' extension.");

        return FileRequest.builder()
                .URL(URL)
                .chatId(update.getChatId())
                .extension(extension)
                .multiVideoNumber(multiVideoNumber)
                .premium(premium)
                .editMessageText(editMessageText)
                .id3TagData(id3TagData)
                .build();
    }

    public String removeUrl(String text) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                text = text.substring(i).trim();
                break;
            }
        }
        return text;
    }


    public String replaceDashesTo11DASH11(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '-') {
                for (int j = 0; j < 4 && j < text.length(); j++) {
                    sb.append(text.charAt(i + 1 + j));
                }
                if (!sb.toString().equals("tag ") && !sb.toString().equals("ext ") && !sb.toString().equals("mul ")) {
                    sb.setLength(0);
                    for (int k = 0; k < text.length(); k++) {
                        if (k == i)
                            sb.append("11DASH11");
                        else
                            sb.append(text.charAt(k));
                    }
                    text = sb.toString();
                    sb.setLength(0);
                }
            }
        }
        return text;
    }

    public String replace11DASH11ToDashes(String text) {
        return text.replaceAll("11DASH11", "-");
    }


    public boolean getPremium(String userId) {
        return userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);
    }


    public ID3TagData getId3Tag(String text) {
        // -tag artist:title:album:releaseYear:genre
        text = replace11DASH11ToDashes(text);
        if (text.isBlank())
            return null;

        ID3TagData id3TagData = null;
        String[] textArray = text.split(":");

        for (int i = 0; i < textArray.length; i++) {
            if (textArray[i].isBlank())
                continue;

            if (id3TagData == null)
                id3TagData = new ID3TagData();

            if (i == 0)
                id3TagData.setArtist(textArray[i]);
            else if (i == 1)
                id3TagData.setTitle(textArray[i]);
            else if (i == 2)
                id3TagData.setAlbum(textArray[i]);
            else if (i == 3)
                id3TagData.setReleaseYear(textArray[i]);
            else if (i == 4) {
                int genre = ID3v1Genres.matchGenreDescription(textArray[i]);
                if (genre == -1)
                    throw new InvalidBotRequestException(info.getWrongGenre(), "[ID3Tag] User specified wrong genre.");
                id3TagData.setGenre(genre);
            }
        }

        return id3TagData;
    }


    public EditMessageText getEditMessageText(String chatId, int messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();
    }


    public int getMultiVideoNumber(String text) {
        int number = 0;
        try {
            number = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            throw new InvalidBotRequestException(
                    info.getWrongMultiVideoNumber(),
                    "User specify wrong multi-video number.");
        }
        return Math.max(number, 0);
    }


    public String getExtension(String text, List<String> availableExtensions) {
        if (availableExtensions.contains(text))
            return text;
        else
            throw new InvalidBotRequestException(
                    info.getWrongExtension(),
                    String.format("User specify wrong extension '%s'.", text));
    }

    public String getURL(String text) {
        return Arrays.stream(text.split(" ")).findFirst().orElse("");
    }
}
