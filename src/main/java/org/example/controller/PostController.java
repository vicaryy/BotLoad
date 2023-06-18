package org.example.controller;

import org.example.api_object.User;
import org.example.api_request.*;
import org.example.api_object.message.Message;
import org.example.configuration.BotInfo;
import org.example.end_point.EndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class PostController {
    private final WebClient client;

    @Autowired
    public PostController(WebClient client) {
        this.client = client;
    }

    public Message execute(SendMessage sendMessage) {
        String url = BotInfo.GET_URL() + EndPoint.SEND_MESSAGE.getPath();

        return client
                .post()
                .uri(url)
                .bodyValue(sendMessage)
                .retrieve()
                .bodyToMono(Message.class)
                .block();
    }

    public Message execute(SendAudio sendAudio) {
        String url = BotInfo.GET_URL() + EndPoint.SEND_AUDIO.getPath();
        String chat_id = sendAudio.getChatId();

        FileSystemResource fileToSend = new FileSystemResource(sendAudio.getAudio().getFile());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("chat_id", chat_id);
        builder.part("audio", fileToSend);

        return client
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Message.class)
                .block();
    }

    public Message execute(SendAnimation sendAnimation) {
        String url = BotInfo.GET_URL() + EndPoint.SEND_ANIMATION.getPath();
        String chat_id = sendAnimation.getChatId();

        FileSystemResource fileToSend = new FileSystemResource(sendAnimation.getAnimation().getFile());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("chat_id", chat_id);
        builder.part("animation", fileToSend);

        return client
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(Message.class)
                .block();
    }

    public <T> T executeMethod(ApiRequest apiRequest) {
        T returnObject = apiRequest.returnObject();

        return returnObject;
    }
}
