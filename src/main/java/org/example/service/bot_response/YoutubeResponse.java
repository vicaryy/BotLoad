package org.example.service.bot_response;

import lombok.RequiredArgsConstructor;
import org.example.api_object.Update;
import org.example.api_request.InputFile;
import org.example.api_request.send.SendAudio;
import org.example.service.RequestService;
import org.example.service.youtube.YoutubeLink;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeResponse {
    private final YoutubeLink youtubeLink;
    private final RequestService requestService;

    public void response(Update update) {
        String link = update.getMessage().getText();

        InputFile audio = youtubeLink.getAudio(link);

        if (audio != null) {
            SendAudio sendAudio = SendAudio.builder()
                    .chatId(update.getChatId())
                    .audio(audio)
                    .build();

            String audioName = audio.getFile().getName();

            try {
                requestService.sendRequest(sendAudio);
                System.out.printf("\n[send] File sent successfully.\n", audioName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            youtubeLink.deleteAudio(audio);
        }
    }
}
