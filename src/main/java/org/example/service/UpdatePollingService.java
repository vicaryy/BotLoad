package org.example.service;

import org.example.api_request.SendMessage;
import org.example.api_response.Update;
import org.example.configuration.ApiBotConfiguration;
import org.example.controller.PostSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UpdatePollingService {
    private final ApiBotConfiguration botConfiguration;
    private final RestTemplate restTemplate;
    private final PostSender sender;

    @Autowired
    public UpdatePollingService(ApiBotConfiguration botConfiguration,
                                RestTemplate restTemplate,
                                PostSender sender) {
        this.botConfiguration = botConfiguration;
        this.restTemplate = restTemplate;
        this.sender = sender;
    }

    public void updateReceiver(Update update) {
        SendMessage message1 = SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text(update.getMessage().getText())
                .build();
        System.out.println(update.getMessage().getChat().getId());

        SendMessage message2 = SendMessage.builder()
                .chatId(update.getMessage().getChat().getId())
                .text("spadaka")
                .build();

//        File someAudio = new File("/Users/vicary/desktop/test.mp3");
//        SendAudio audio = SendAudio.builder()
//                .chatId(update.getMessage().getChat().getId())
//                .audio(new InputFile("MUZYKA", someAudio))
//                .build();

//        System.out.println(audio);
        sender.execute(message1);
        sender.executeAnimation();
    }
}
