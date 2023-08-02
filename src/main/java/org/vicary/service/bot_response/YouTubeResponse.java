package org.vicary.service.bot_response;

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

    private void sendMp3(YouTubeFileRequest request) throws Exception {
        // preparing message and chat action to send
        String downloadMessage = "Downloading file...";
        String downloadMessage1 = "There you go.";
        Message botMessageInfo;
        SendMessage sendMessage = SendMessage.builder()
                .chatId(request.getChatId())
                .disableNotification(true)
                .text(downloadMessage)
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
                .text(downloadMessage)
                .build();
        request.setEditMessageText(editMessageText);

        // preparing InputFiles
        InputFile downloadedFile;
        InputFile thumbnail;

        // checks if file already exists in repository
        downloadedFile = getFileFromRepository(request);

        // downloading file from YouTube if does not exist in repo
        if (downloadedFile == null)
            downloadedFile = youtubeDownloader.downloadMp3(request);

        Message sendFileMessage = null;
        if (downloadedFile != null) {
            // downloading thumbnail
            thumbnail = youtubeDownloader.downloadThumbnail(request.getYoutubeId());

            // preparing audio object to send
            SendAudio sendAudio = SendAudio.builder()
                    .chatId(request.getChatId())
                    .audio(downloadedFile)
                    .thumbnail(thumbnail)
                    .build();

            // sending audio to telegram chat
            System.out.printf("\n[send] Sending file to chatId: %s", request.getChatId());
            requestService.sendRequest(sendChatAction);
            sendFileMessage = requestService.sendRequest(sendAudio);
            editMessageText.setText(downloadMessage1);
            requestService.sendRequestAsync(editMessageText);
            System.out.printf("\n[send] File sent successfully.\n");

            // deleting thumbnail
            if (thumbnail != null)
                youtubeDownloader.deleteFile(thumbnail);
        }

        // saving file to repository
        if (sendFileMessage != null && downloadedFile.getFileId() == null) {
            youtubeDownloader.deleteFile(downloadedFile);

            String title = downloadedFile.getFile().getName();
            String extension = request.getExtension();
            saveFileToRepository(YouTubeFileEntity.builder()
                    .youtubeId(request.getYoutubeId())
                    .extension(request.getExtension())
                    .quality(request.getPremium() ? "premium" : "standard")
                    .size(convertKBToMB(sendFileMessage.getAudio().getFileSize()))
                    .duration(convertSecondsToMinutes(sendFileMessage.getAudio().getDuration()))
                    .title(title.substring(0, title.length() - (extension.length() + 1)))
                    .fileId(sendFileMessage.getAudio().getFileId())
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
