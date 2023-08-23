package org.vicary.service.response;

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
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.pattern.TwitterPattern;
import org.vicary.service.*;
import org.vicary.service.downloader.TwitterDownloader;
import org.vicary.service.file_service.TwitterFileService;
import org.vicary.service.quick_sender.QuickSender;

@Service
@RequiredArgsConstructor
public class TwitterResponse {
    private final static Logger logger = LoggerFactory.getLogger(TwitterResponse.class);

    private final TwitterDownloader twitterDownloader;

    private final ResponseInfo info;

    private final TwitterFileService twitterFileService;

    private final RequestService requestService;

    private final UserService userService;

    private final QuickSender quickSender;

    private final static String EXTENSION = "mp4";

    public void response(Update update) throws Exception {
        final String chatId = update.getChatId();
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final String twitterURL = TwitterPattern.getURL(text);
        final int multiVideoNumber = getMultiVideoNumber(text);
        final boolean premium = userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);

        quickSender.chatAction(chatId, "typing");
        Message botMessageInfo = quickSender.messageWithReturn(chatId, info.getGotTheLink() + info.getHoldOn(), true);
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(chatId)
                .messageId(botMessageInfo.getMessageId())
                .text(info.getGotTheLink() + info.getHoldOn())
                .parseMode("MarkdownV2")
                .disableWebPagePreview(true)
                .build();

        final FileRequest request = FileRequest.builder()
                .URL(twitterURL)
                .chatId(chatId)
                .premium(false)
                .extension(EXTENSION)
                .multiVideoNumber(multiVideoNumber)
                .editMessageText(editMessageText)
                .build();

        sendFile(request);
    }

    public void sendFile(FileRequest request) throws Exception {
        // getting youtube file
        FileResponse response = twitterDownloader.download(request);

        // preparing video to send
        SendVideo sendVideo = SendVideo.builder()
                .chatId(request.getChatId())
                .video(response.getDownloadedFile())
                .duration(response.getDuration())
                .build();

        // sending document to telegram chat
        logger.info("[send] Sending file '{}' to chatId '{}'", response.getId(), request.getChatId());
        quickSender.chatAction(request.getChatId(), "upload_document");
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getSending());
        Message sendFileMessage = requestService.sendRequest(sendVideo);
        quickSender.editMessageText(request.getEditMessageText(), getReceivedFileInfo(response));
        logger.info("[send] File sent successfully.");

        // saving file to repository
        if (!twitterFileService.existsByTwitterId(response.getId())) {
            twitterFileService.saveEntity(TwitterFileEntity.builder()
                    .twitterId(response.getId())
                    .extension(response.getExtension())
                    .quality(response.isPremium() ? "premium" : "standard")
                    .size(Converter.bytesToMB(response.getSize()))
                    .duration(Converter.secondsToMinutes(response.getDuration()))
                    .title(response.getTitle())
                    .URL(response.getURL())
                    .fileId(sendFileMessage.getVideo().getFileId())
                    .build());
        }
        // deleting downloaded files
        if (response.getDownloadedFile().getFile() != null)
            TerminalExecutor.removeFile(response.getDownloadedFile().getFile());
    }

    public String getReceivedFileInfo(FileResponse response) {
        final int maxTitleLength = 250;
        String title = response.getTitle();
        final String duration = Converter.secondsToMinutes(response.getDuration());
        final String size = Converter.bytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.isPremium() ? "Premium" : "Standard";

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

    public int getMultiVideoNumber(String text) {
        String[] array = text.split(" ");
        int number = 0;
        if (array.length > 1) {
            String multiVideo = array[1];
            if (multiVideo.startsWith("#")) {
                multiVideo = multiVideo.substring(1);
                try {
                        number = Integer.parseInt(multiVideo);
                } catch (NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                    logger.info("User type wrong multi-video number '{}'.", array[1]);
                }
            }
        }
        return Math.max(number, 0);
    }
}
