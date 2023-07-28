package org.example.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_request.InputFile;
import org.example.api_request.send.SendAudio;
import org.example.api_request.send.SendDocument;
import org.example.service.RequestService;
import org.example.service.youtube.YoutubeDownloader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeResponse {
    private final YoutubeDownloader youtubeDownloader;
    private final RequestService requestService;

    public void response(Update update) {
        String chatId = update.getChatId();
        String text = update.getMessage().getText();

        String youtubeLink = getLink(text);
        String extension = getExtension(text);
        String videoId = getVideoId(youtubeLink);

        System.out.println("Youtube link: " + youtubeLink);
        System.out.println("Extension: " + extension);
        System.out.println("videoId: " + videoId);

        if (extension.equals("mp3"))
            sendMp3(videoId, chatId);
        else if (extension.equals("mp4"))
            sendMp4(youtubeLink, chatId);
        else if (extension.equals("m4a"))
            sendM4a(youtubeLink, chatId);
    }

    private String getLink(String text) {
        String[] textArray = text.trim().split(" ");
        return textArray[0];
    }

    private String getExtension(String text) {
        String[] textArray = text.trim().split(" ");
        if (textArray.length > 1)
            return textArray[1].toLowerCase();
        return "mp3";
    }

    public String getVideoId(String youtubeLink) {
        StringBuffer sb = new StringBuffer();
        if (youtubeLink.startsWith("https://www.youtube.com/watch?v=")) {
            youtubeLink = youtubeLink.substring(32);
            for (char s : youtubeLink.toCharArray()) {
                if (s == '&')
                    break;
                sb.append(s);
            }
        } else if (youtubeLink.startsWith("https://youtu.be/")) {
            youtubeLink = youtubeLink.substring(17);
            for (char s : youtubeLink.toCharArray()) {
                if (s == '&')
                    break;
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private void sendMp3(String videoId, String chatId) {
        List<InputFile> inputFiles = youtubeDownloader.getMp3(videoId);

        if (!inputFiles.isEmpty()) {
            SendAudio sendAudio = SendAudio.builder()
                    .chatId(chatId)
                    .audio(inputFiles.get(0))
                    .build();
            if (inputFiles.size() > 1)
                sendAudio.setThumbnail(inputFiles.get(1));

            try {
                System.out.printf("\n[send] Sending file to chatId: %s", chatId);
                requestService.sendRequest(sendAudio);
                System.out.printf("\n[send] File sent successfully.\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtubeDownloader.deleteFile(inputFiles.get(0));
            if (inputFiles.size() > 1)
                youtubeDownloader.deleteFile(inputFiles.get(1));
        }
    }

    private void sendM4a(String link, String chatId) {
        InputFile m4a = youtubeDownloader.getM4a(link);

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
        InputFile mp4 = youtubeDownloader.getMp4(link);

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
