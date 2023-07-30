package org.example.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_object.message.Message;
import org.example.api_request.InputFile;
import org.example.api_request.send.SendAudio;
import org.example.api_request.send.SendDocument;
import org.example.entity.YouTubeFileEntity;
import org.example.pattern.YoutubePattern;
import org.example.repository.UserRepository;
import org.example.repository.YoutubeFileRepository;
import org.example.service.RequestService;
import org.example.service.mapper.YouTubeFileMapper;
import org.example.service.youtube.YouTubeDownloader;
import org.example.service.youtube.YouTubeFileRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YouTubeResponse {
    private final YouTubeDownloader youtubeDownloader;
    private final RequestService requestService;
    private final YoutubeFileRepository youtubeFileRepository;
    private final UserRepository userRepository;
    private final YouTubeFileMapper mapper;

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
        InputFile file = null;
        InputFile thumbnail = null;
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

            saveFileToRepository(YouTubeFileEntity.builder()
                    .youtubeId(request.getYoutubeId())
                    .extension(request.getExtension())
                    .quality(request.getPremium() ? "premium" : "standard")
                    .size(sizeToString(message.getAudio().getFileSize()))
                    .duration(durationToString(message.getAudio().getDuration()))
                    .title(file.getFile().getName())
                    .fileId(message.getAudio().getFileId())
                    .build());
        }
    }

    private void saveFileToRepository(YouTubeFileEntity youTubeFileEntity) {
        youtubeFileRepository.save(youTubeFileEntity);
    }

    private String sizeToString(int fileSize) {
        double sizeInMB = (double) fileSize / (1024 * 1024);
        return String.format("%.2fMB", sizeInMB);
    }

    private String durationToString(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
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
