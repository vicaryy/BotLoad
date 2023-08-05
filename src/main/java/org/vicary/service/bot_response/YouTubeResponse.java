package org.vicary.service.bot_response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendChatAction;
import org.vicary.api_request.send.SendDocument;
import org.vicary.api_request.send.SendMessage;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.format.MarkdownV2;
import org.vicary.model.YouTubeFileResponse;
import org.vicary.pattern.YoutubePattern;
import org.vicary.repository.UserRepository;
import org.vicary.repository.YoutubeFileRepository;
import org.vicary.service.RequestService;
import org.vicary.service.youtube.YouTubeDownloader;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeResponse {

    private final YouTubeDownloader youtubeDownloader;

    private final RequestService requestService;

    private final YoutubeFileRepository youtubeFileRepository;

    private final UserRepository userRepository;

    public void response(Update update) throws Exception {
        final String chatId = update.getChatId();
        final String text = update.getMessage().getText();
        final String userId = update.getMessage().getFrom().getId().toString();
        final String extension = getExtension(text);
        final String youtubeId = YoutubePattern.getYoutubeId(text);
        final boolean premium = userRepository.findByUserId(userId).getPremium();

        final YouTubeFileRequest request = YouTubeFileRequest.builder()
                .youtubeId(youtubeId)
                .chatId(chatId)
                .extension(extension)
                .premium(premium)
                .build();

        if (extension.equals("mp3"))
            sendMp3(request);
        else if (extension.equals("mp4"))
            sendMp4(youtubeId, chatId);
        else if (extension.equals("m4a"))
            sendM4a(youtubeId, chatId);
    }

    private void sendMp3(YouTubeFileRequest request) throws Exception {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // preparing message and chat action to send
        final String gotTheLinkInfo = MarkdownV2.apply("Got the link!").toBold().newlineAfter().get();
        final String holdOnInfo = MarkdownV2.apply("Just hold on for a moment.").newlineAfter().get();
        final String sendingInfo = MarkdownV2.apply("Sending...").toItalic().newlineBefore().get();
        final String errorInfo = MarkdownV2.apply("Sorry but something goes wrong.").toBold().get();
        final String receivedInfo = MarkdownV2.apply("Here's your file.").get();

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
        YouTubeFileResponse response = youtubeDownloader.downloadMp3(request);

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
            if (sendFileMessage != null && !youtubeFileRepository.existsByYoutubeIdAndExtensionAndQuality(response.getYoutubeId(), response.getExtension(), response.getPremium() ? "premium" : "standard")) {
                youtubeDownloader.deleteFile(response.getDownloadedFile());

                saveFileToRepository(YouTubeFileEntity.builder()
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
        final String receivedInfo = "Here's your file: ";
        String text = response.getEditMessageText().getText();

        String title = response.getTitle();
        String artist = response.getArtist();
        String track = response.getTrack();
        String album = response.getAlbum();
        String releaseYear = response.getReleaseYear();
        String duration = convertSecondsToMinutes(response.getDuration());
        String size = convertBytesToMB(response.getSize());
        String quality = response.getPremium() ? "Premium" : "Standard";

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

    private void saveFileToRepository(YouTubeFileEntity youTubeFileEntity) {
        youtubeFileRepository.save(youTubeFileEntity);
    }

    private String convertBytesToMB(Long Bytes) {
        double sizeInMB = (double) Bytes / (1024 * 1024);
        return String.format("%.2fMB", sizeInMB);
    }

    private String convertSecondsToMinutes(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        return String.format("%d:%02d", minutes, sec);
    }

    private void sendM4a(String link, String chatId) {
        InputFile m4a = youtubeDownloader.downloadM4a(link);

        if (m4a != null) {
            SendAudio sendAudio = SendAudio.builder()
                    .chatId(chatId)
                    .audio(m4a)
                    .build();

            String m4aName = m4a.getFile().getName();

            try {
                System.out.printf("\n[send] Sending file to chatId: %s", chatId);
                requestService.sendRequest(sendAudio);
                System.out.printf("\n[send] File sent successfully.\n", m4aName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtubeDownloader.deleteFile(m4a);
        }
    }

    private void sendMp4(String link, String chatId) {
        InputFile mp4 = youtubeDownloader.downloadMp4(link);

        if (mp4 != null) {
            SendDocument sendDocument = SendDocument.builder()
                    .chatId(chatId)
                    .document(mp4)
                    .build();

            String mp4Name = mp4.getFile().getName();
            try {
                System.out.printf("\n[send] Sending file to chatId: %s", chatId);
                requestService.sendRequest(sendDocument);
                System.out.printf("\n[send] File sent successfully.\n", mp4Name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtubeDownloader.deleteFile(mp4);
        }
    }
}
