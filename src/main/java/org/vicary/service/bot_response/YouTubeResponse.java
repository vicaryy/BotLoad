package org.vicary.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.vicary.api_object.Update;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.send.SendAudio;
import org.vicary.api_request.send.SendChatAction;
import org.vicary.api_request.send.SendDocument;
import org.vicary.api_request.send.SendMessage;
import org.vicary.entity.YouTubeFileEntity;
import org.vicary.pattern.YoutubePattern;
import org.vicary.repository.UserRepository;
import org.vicary.repository.YoutubeFileRepository;
import org.vicary.service.RequestService;
import org.vicary.service.youtube.YouTubeDownloader;
import org.vicary.model.YouTubeFileRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YouTubeResponse {
    private final YouTubeDownloader youtubeDownloader;
    private final RequestService requestService;
    private final YoutubeFileRepository youtubeFileRepository;
    private final UserRepository userRepository;

    public void response(Update update) {
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

    private String getExtension(String text) {
        String[] textArray = text.trim().split(" ");
        if (textArray.length > 1)
            return textArray[1].toLowerCase();
        return "mp3";
    }

    private InputFile getFileFromRepository(YouTubeFileRequest request) {
        YouTubeFileEntity youTubeFileEntity = youtubeFileRepository.findByYoutubeIdAndExtensionAndQuality(
                request.getYoutubeId(),
                request.getExtension(),
                request.getPremium() ? "premium" : "standard");
        if (youTubeFileEntity != null) {
            return InputFile.builder()
                    .fileId(youTubeFileEntity.getFileId())
                    .build();
        }
        return null;
    }

    private void sendMp3(YouTubeFileRequest request) {
        String text = String.format("Downloading your %s file in %s quality, give me a moment...", request.getExtension(), request.getPremium() ? "premium" : "standard");
        SendMessage sendMessage = SendMessage.builder()
                .chatId(request.getChatId())
                .disableNotification(true)
                .text(text)
                .build();
        SendChatAction sendChatAction = SendChatAction.builder()
                .chatId(request.getChatId())
                .build();
        sendChatAction.setActionOnUploadDocument();
        try {
            requestService.sendRequest(sendMessage);
            requestService.sendRequest(sendChatAction);
        } catch (Exception e) {
        }

        InputFile file;
        InputFile thumbnail;
        Message message = null;

        file = getFileFromRepository(request);

        if (file == null)
            file = youtubeDownloader.downloadMp3(request);

        if (file != null) {
            thumbnail = youtubeDownloader.downloadThumbnail(request.getYoutubeId());

            SendAudio sendAudio = SendAudio.builder()
                    .chatId(request.getChatId())
                    .audio(file)
                    .thumbnail(thumbnail)
                    .build();

            try {
                System.out.printf("\n[send] Sending file to chatId: %s", request.getChatId());
                message = requestService.sendRequest(sendAudio);
                System.out.printf("\n[send] File sent successfully.\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (thumbnail != null)
                youtubeDownloader.deleteFile(thumbnail);
        }
        if (message != null && file.getFileId() == null) {
            youtubeDownloader.deleteFile(file);

            String title = file.getFile().getName();
            String extension = request.getExtension();
            saveFileToRepository(YouTubeFileEntity.builder()
                    .youtubeId(request.getYoutubeId())
                    .extension(request.getExtension())
                    .quality(request.getPremium() ? "premium" : "standard")
                    .size(convertKBToMB(message.getAudio().getFileSize()))
                    .duration(convertSecondsToMinutes(message.getAudio().getDuration()))
                    .title(title.substring(0, title.length() - (extension.length() + 1)))
                    .fileId(message.getAudio().getFileId())
                    .build());
        }
    }

    private void saveFileToRepository(YouTubeFileEntity youTubeFileEntity) {
        youtubeFileRepository.save(youTubeFileEntity);
    }

    private String convertKBToMB(int KB) {
        double sizeInMB = (double) KB / (1024 * 1024);
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
