package org.example.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_request.InputFile;
import org.example.api_request.send.SendAudio;
import org.example.api_request.send.SendDocument;
import org.example.service.RequestService;
import org.example.service.youtube.YoutubeDownloader;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeResponse {
    private final YoutubeDownloader youtubeDownloader;
    private final RequestService requestService;

    public void response(Update update) {
        String text = update.getMessage().getText();
        String[] textArray = text.trim().split(" ");
        String link = textArray[0];
        String extension = "mp3";
        String chatId = update.getChatId();
        for (int i = 0; i < textArray.length; i++)
            if (i == 1) {
                extension = textArray[1].toLowerCase();
                break;
            }

        if (extension.equals("mp3"))
            sendMp3(link, chatId);
        else if (extension.equals("mp4"))
            sendMp4(link, chatId);
        else if (extension.equals("m4a"))
            sendM4a(link, chatId);
    }



    private void sendMp3(String link, String chatId) {
        InputFile mp3 = youtubeDownloader.getMp3(link);

        if (mp3 != null) {
            SendAudio sendAudio = SendAudio.builder()
                    .chatId(chatId)
                    .audio(mp3)
                    .build();

            String mp3Name = mp3.getFile().getName();

            try {
                System.out.printf("\n[send] Sending file to chatId: %s", chatId);
                requestService.sendRequest(sendAudio);
                System.out.printf("\n[send] File sent successfully.\n", mp3Name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtubeDownloader.deleteFile(mp3);
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
