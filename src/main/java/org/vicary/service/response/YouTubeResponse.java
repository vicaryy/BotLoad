package org.vicary.service.response;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vicary.api_object.Update;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendAudio;
import org.vicary.entity.UserEntity;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.info.ResponseInfo;
import org.vicary.model.FileRequest;
import org.vicary.model.FileResponse;
import org.vicary.service.*;
import org.vicary.service.file_service.YouTubeFileService;
import org.vicary.service.quick_sender.QuickSender;
import org.vicary.pattern.YoutubePattern;
import org.vicary.service.downloader.YouTubeDownloader;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class YouTubeResponse {
    private final static Logger logger = LoggerFactory.getLogger(YouTubeResponse.class);

    private final YouTubeDownloader youtubeDownloader;

    private final ResponseInfo info;

    private final YouTubeFileService youTubeFileService;

    private final RequestService requestService;

    private final UserService userService;

    private final QuickSender quickSender;
    private final Set<String> availableExtensions = Set.of("mp3", "m4a", "flac");

    public void response(Update update) throws Exception {
        final String chatId = update.getChatId();
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final String extension = getExtension(text);
        final String youtubeURL = YoutubePattern.getUrl(text);
        final boolean premium = userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);

        final FileRequest request = FileRequest.builder()
                .URL(youtubeURL)
                .chatId(chatId)
                .extension(extension)
                .premium(premium)
                .build();

        if (availableExtensions.contains(extension))
            sendFile(request);
        else
            quickSender.message(chatId, info.getWrongExtension(), false);
    }

    public void sendFile(FileRequest request) throws Exception {
        // preparing message and chat action to send
        final String chatId = request.getChatId();

        Message botMessageInfo = quickSender.messageWithReturn(chatId, info.getGotTheLink() + info.getHoldOn(), true);
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
        FileResponse response = youtubeDownloader.download(request);

        // preparing audio object to send
        SendAudio sendAudio = SendAudio.builder()
                .chatId(request.getChatId())
                .audio(response.getDownloadedFile())
                .thumbnail(response.getThumbnail())
                .title(response.getTrack())
                .performer(response.getArtist())
                .duration(response.getDuration())
                .build();

        // sending audio to telegram chat
        logger.info("[send] Sending file '{}' to chatId '{}'", response.getId(), chatId);
        quickSender.chatAction(chatId, "upload_document");
        quickSender.editMessageText(request.getEditMessageText(), request.getEditMessageText().getText() + info.getSending());
        Message sendFileMessage = requestService.sendRequest(sendAudio);
        quickSender.editMessageText(editMessageText, getReceivedFileInfo(response));
        logger.info("[send] File sent successfully.");

        // saving file to repository
        if (!youTubeFileService.existsInRepo(response)) {
            youTubeFileService.saveYouTubeFile(YouTubeFileEntity.builder()
                    .youtubeId(response.getId())
                    .extension(response.getExtension())
                    .quality(response.isPremium() ? "premium" : "standard")
                    .size(Converter.bytesToMB(response.getSize()))
                    .duration(Converter.secondsToMinutes(response.getDuration()))
                    .title(response.getTitle())
                    .fileId(sendFileMessage.getAudio().getFileId())
                    .build());
        }

        // deleting downloaded files
        if (response.getDownloadedFile().getFile() != null)
            TerminalExecutor.removeFile(response.getDownloadedFile().getFile());
        TerminalExecutor.removeFile(response.getThumbnail().getFile());
    }

    public String getReceivedFileInfo(FileResponse response) {
        StringBuilder fileInfo = new StringBuilder();

        final String title = response.getTitle();
        final String artist = response.getArtist();
        final String track = response.getTrack();
        final String album = response.getAlbum();
        final String releaseYear = response.getReleaseYear();
        final String duration = Converter.secondsToMinutes(response.getDuration());
        final String size = Converter.bytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.isPremium() ? "Premium" : "Standard";

        fileInfo.append(MarkdownV2.apply(info.getReceived()).toItalic().newlineAfter().get());
        if (track == null) {
            fileInfo.append(info.getTitle());
            fileInfo.append(MarkdownV2.apply(title).get());
        }
        if (artist != null) {
            fileInfo.append(info.getArtist());
            fileInfo.append(MarkdownV2.apply(artist).get());
        }
        if (track != null) {
            fileInfo.append(info.getTrack());
            fileInfo.append(MarkdownV2.apply(track).get());
        }
        if (album != null) {
            fileInfo.append(info.getAlbum());
            fileInfo.append(MarkdownV2.apply(album).get());
        }
        if (releaseYear != null) {
            fileInfo.append(info.getReleaseYear());
            fileInfo.append(MarkdownV2.apply(releaseYear).get());
        }
        if (duration != null) {
            fileInfo.append(info.getDuration());
            fileInfo.append(MarkdownV2.apply(duration).get());
        }
        if (size != null) {
            fileInfo.append(info.getSize());
            fileInfo.append(MarkdownV2.apply(size).get());
        }
        if (extension != null) {
            fileInfo.append(info.getExtension());
            fileInfo.append(MarkdownV2.apply(extension).get());
        }
        fileInfo.append(info.getQuality());
        fileInfo.append(MarkdownV2.apply(quality).get());

        return fileInfo.toString();
    }

    public String getExtension(String text) {
        String[] textArray = text.trim().split(" ");
        if (textArray.length > 1)
            return textArray[1].toLowerCase();
        return "mp3";
    }
}
