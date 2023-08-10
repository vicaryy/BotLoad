package org.vicary.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendChatAction;
import org.vicary.api_request.send.SendMessage;
import org.vicary.entity.UserEntity;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.model.YouTubeFileResponse;
import org.vicary.service.youtube.YoutubePattern;
import org.vicary.service.RequestService;
import org.vicary.service.UserService;
import org.vicary.service.YouTubeFileService;
import org.vicary.service.youtube.YouTubeDownloader;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class YouTubeResponse {

    private final YouTubeDownloader youtubeDownloader;

    private final RequestService requestService;

    private final YouTubeFileService youTubeFileService;

    private final UserService userService;
    private final Set<String> availableExtensions = Set.of("mp3", "m4a", "flac");

    public void response(Update update) throws Exception {
        final String chatId = update.getChatId();
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final String extension = getExtension(text);
        final String youtubeUrl = Arrays.stream(text.split(" ")).findFirst().get();
        final String youtubeId = YoutubePattern.getYoutubeId(youtubeUrl);
        final boolean premium = userService.findByUserId(userId)
                .map(UserEntity::getPremium)
                .orElse(false);

        final YouTubeFileRequest request = YouTubeFileRequest.builder()
                .youtubeId(youtubeId)
                .chatId(chatId)
                .extension(extension)
                .premium(premium)
                .build();

        if (availableExtensions.contains(extension))
            sendFile(request);
        else {
            requestService.sendRequestAsync(SendMessage.builder()
                    .chatId(chatId)
                    .text("Sorry but I can't identify the extension.")
                    .build());
        }
    }

    public void sendFile(YouTubeFileRequest request) throws Exception {
        // preparing message and chat action to send
        final String gotTheLinkInfo = MarkdownV2.apply("Got the link!").toBold().newlineAfter().get();
        final String holdOnInfo = MarkdownV2.apply("Just hold on for a moment.").newlineAfter().get();
        final String sendingInfo = MarkdownV2.apply("Sending...").toItalic().newlineBefore().get();
        final String errorInfo = MarkdownV2.apply("Sorry but something goes wrong.").toBold().get();

        Message botMessageInfo;
        SendMessage sendMessage = SendMessage.builder()
                .chatId(request.getChatId())
                .disableNotification(true)
                .text(gotTheLinkInfo + holdOnInfo)
                .parseMode("MarkdownV2")
                .build();
        SendChatAction sendChatAction = SendChatAction.builder()
                .chatId(request.getChatId())
                .action("typing")
                .build();

        // sending message and chat action
        botMessageInfo = requestService.sendRequest(sendMessage);
        requestService.sendRequest(sendChatAction);

        // setting editMessageText to request
        EditMessageText editMessageText = EditMessageText.builder()
                .chatId(request.getChatId())
                .messageId(botMessageInfo.getMessageId())
                .text(gotTheLinkInfo + holdOnInfo)
                .parseMode("MarkdownV2")
                .build();
        request.setEditMessageText(editMessageText);

        // getting youtube file
        YouTubeFileResponse response = youtubeDownloader.download(request);

        System.out.println(response.getDownloadedFile());
        if (response.getDownloadedFile() != null) {
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
            System.out.printf("\n[send] Sending file to chatId: %s", request.getChatId());
            request.getEditMessageText().setText(request.getEditMessageText().getText() + sendingInfo);
            sendChatAction.setActionOnUploadDocument();
            requestService.sendRequest(sendChatAction);
            requestService.sendRequestAsync(request.getEditMessageText());
            Message sendFileMessage = requestService.sendRequest(sendAudio);
            editMessageText.setText(getReceivedFileInfo(response));
            requestService.sendRequestAsync(editMessageText);
            System.out.printf("\n[send] File sent successfully.\n");

            // deleting thumbnail
            if (response.getThumbnail() != null)
                youtubeDownloader.deleteFile(response.getThumbnail());

            // saving file to repository
            if (sendFileMessage != null && !youTubeFileService.existsByYoutubeIdAndExtensionAndQuality(response.getYoutubeId(), response.getExtension(), response.getPremium() ? "premium" : "standard")) {
                youtubeDownloader.deleteFile(response.getDownloadedFile());

                youTubeFileService.saveYouTubeFile(YouTubeFileEntity.builder()
                        .youtubeId(request.getYoutubeId())
                        .extension(request.getExtension())
                        .quality(request.getPremium() ? "premium" : "standard")
                        .size(convertBytesToMB(response.getSize()))
                        .duration(convertSecondsToMinutes(response.getDuration()))
                        .title(response.getTitle())
                        .fileId(sendFileMessage.getAudio().getFileId())
                        .build());
            }
        } else {
            request.getEditMessageText().setText(errorInfo);
            requestService.sendRequestAsync(request.getEditMessageText());
        }
    }

    public String getReceivedFileInfo(YouTubeFileResponse response) {
        StringBuilder fileInfo = new StringBuilder();

        final String receivedInfo = "Here's your file";
        final String title = response.getTitle();
        final String artist = response.getArtist();
        final String track = response.getTrack();
        final String album = response.getAlbum();
        final String releaseYear = response.getReleaseYear();
        final String duration = convertSecondsToMinutes(response.getDuration());
        final String size = convertBytesToMB(response.getSize());
        final String extension = response.getExtension();
        final String quality = response.getPremium() ? "Premium" : "Standard";

        fileInfo.append(MarkdownV2.apply(receivedInfo).toItalic().newlineAfter().get());
        if (track == null) {
            fileInfo.append(MarkdownV2.apply("Title: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(title).get());
        }
        if (artist != null) {
            fileInfo.append(MarkdownV2.apply("Artist: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(artist).get());
        }
        if (track != null) {
            fileInfo.append(MarkdownV2.apply("Track: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(track).get());
        }
        if (album != null) {
            fileInfo.append(MarkdownV2.apply("Album: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(album).get());
        }
        if (releaseYear != null) {
            fileInfo.append(MarkdownV2.apply("Release Year: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(releaseYear).get());
        }
        if (duration != null) {
            fileInfo.append(MarkdownV2.apply("Duration: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(duration).get());
        }
        if (size != null) {
            fileInfo.append(MarkdownV2.apply("Size: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(size).get());
        }
        if (extension != null) {
            fileInfo.append(MarkdownV2.apply("Extension: ").toBold().newlineBefore().get());
            fileInfo.append(MarkdownV2.apply(extension).get());
        }
        fileInfo.append(MarkdownV2.apply("Quality: ").toBold().newlineBefore().get());
        fileInfo.append(MarkdownV2.apply(quality).get());

        return fileInfo.toString();
    }

    private String getExtension(String text) {
        String[] textArray = text.trim().split(" ");
        if (textArray.length > 1)
            return textArray[1].toLowerCase();
        return "mp3";
    }

    public String convertBytesToMB(Long Bytes) {
        double sizeInMB = (double) Bytes / (1024 * 1024);
        return String.format("%.2fMB", sizeInMB);
    }

    public String convertSecondsToMinutes(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", minutes, sec);
    }
}
