package org.vicary.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vicary.api_object.Update;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendVideo;
import org.vicary.entity.TwitterFileEntity;
import org.vicary.entity.UserEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.YouTubeResponseInfo;
import org.vicary.model.twitter.TwitterFileRequest;
import org.vicary.model.twitter.TwitterFileResponse;
import org.vicary.pattern.twitter.TwitterPattern;
import org.vicary.service.*;
import org.vicary.service.downloader.TwitterDownloader;
import org.vicary.service.quick_sender.QuickSender;

@Service
@RequiredArgsConstructor
public class TwitterResponse {
    private final static Logger logger = LoggerFactory.getLogger(TwitterResponse.class);

    private final TwitterDownloader twitterDownloader;

    private final YouTubeResponseInfo info;

    private final TwitterFileService twitterFileService;

    private final RequestService requestService;

    private final UserService userService;

    private final QuickSender quickSender;

    public void response(Update update) throws Exception {
        final String chatId = update.getChatId();
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final String twitterUrl = TwitterPattern.getUrl(text);
        final boolean premium = userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);

        final TwitterFileRequest request = TwitterFileRequest.builder()
                .url(twitterUrl)
                .chatId(chatId)
                .premium(premium)
                .build();

        sendFile(request);
    }

    public void sendFile(TwitterFileRequest request) throws Exception {
        final String chatId = request.getChatId();

        Message botMessageInfo;
        botMessageInfo = quickSender.messageWithReturn(chatId, info.getGotTheLink() + info.getHoldOn(), true);
        quickSender.chatAction(chatId, "typing");

        // setting editMessageText to request
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(request.getChatId())
                .messageId(botMessageInfo.getMessageId())
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .build();
        request.setEditMessageText(editMessageText);

        // getting youtube file
        TwitterFileResponse response = twitterDownloader.download(request);

        // preparing document to send
//        SendDocument sendDocument = SendDocument.builder()
//                .chatId(request.getChatId())
//                .document(response.getDownloadedFile())
//                .build();
        SendVideo sendVideo = SendVideo.builder()
                .chatId(request.getChatId())
                .video(response.getDownloadedFile())
                .duration(response.getDuration())
                .build();

        // sending document to telegram chat
        logger.info("[send] Sending file '{}' to chatId '{}'", response.getTwitterId(), request.getChatId());
        quickSender.chatAction(chatId, "upload_document");
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getSending());
        Message sendFileMessage = requestService.sendRequest(sendVideo);
        quickSender.editMessageText(editMessageText, getReceivedFileInfo(response));
        logger.info("[send] File sent successfully.");

        // saving file to repository
        if (!twitterFileService.existsByTwitterId(response.getTwitterId())) {
            twitterFileService.saveEntity(TwitterFileEntity.builder()
                    .twitterId(response.getTwitterId())
                    .extension(response.getExtension())
                    .quality(response.getPremium() ? "premium" : "standard")
                    .size(Converter.bytesToMB(response.getSize()))
                    .duration(Converter.secondsToMinutes(response.getDuration()))
                    .title(response.getTitle())
                    .fileId(sendFileMessage.getVideo().getFileId())
                    .build());
        }
        // deleting downloaded files
        if (response.getDownloadedFile().getFile() != null)
            TerminalExecutor.removeFile(response.getDownloadedFile().getFile());
    }

    public String getReceivedFileInfo(TwitterFileResponse response) {
        final int maxTitleLength = 250;
        String title = response.getTitle();
        final String duration = Converter.secondsToMinutes(response.getDuration());
        final String size = Converter.bytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.getPremium() ? "Premium" : "Standard";

        if (response.getTitle().length() > maxTitleLength)
            title = title.substring(0, maxTitleLength) + "...";

        StringBuilder fileInfo = new StringBuilder();
        fileInfo.append(MarkdownV2.apply(info.getReceived()).toItalic().newlineAfter().get());

        fileInfo.append(info.getTitle());
        fileInfo.append(MarkdownV2.apply(title).get());

        fileInfo.append(info.getDuration());
        fileInfo.append(MarkdownV2.apply(duration).get());

        fileInfo.append(info.getSize());
        fileInfo.append(MarkdownV2.apply(size).get());

        fileInfo.append(info.getExtension());
        fileInfo.append(MarkdownV2.apply(extension).get());

        fileInfo.append(info.getQuality());
        fileInfo.append(MarkdownV2.apply(quality).get());

        return fileInfo.toString();
    }
}
