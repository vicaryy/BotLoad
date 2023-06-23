package org.example.controller;

import org.example.api_object.File;
import org.example.api_object.RequestResponse;
import org.example.api_object.message.Message;
import org.example.api_request.*;
import org.example.configuration.BotInfo;
import org.example.configuration.ParameterizedTypeReferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Controller
public class PostController {
    private final WebClient client;
    private final ParameterizedTypeReferences typeReferences;

    @Autowired
    public PostController(WebClient client,
                          ParameterizedTypeReferences typeReferences) {
        this.client = client;
        this.typeReferences = typeReferences;
    }

    public Message sendRequest(SendPhoto sendPhoto) throws Exception {
        sendPhoto.checkValidation();
        String url = BotInfo.GET_URL() + sendPhoto.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendPhoto.getChatId());

        inputFile(sendPhoto.getPhoto(), bodyBuilder, sendPhoto.getMethodName());

        if (sendPhoto.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendPhoto.getMessageThreadId());

        if (sendPhoto.getCaption() != null)
            bodyBuilder.part("caption", sendPhoto.getCaption());

        bodyBuilder.part("parse_mode", sendPhoto.getParseMode());

        if (sendPhoto.getParseMode().equals("") && sendPhoto.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendPhoto.getCaptionEntities());

        if (sendPhoto.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendPhoto.getHasSpoiler());

        if (sendPhoto.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendPhoto.getDisableNotification());

        if (sendPhoto.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendPhoto.getProtectContent());

        if (sendPhoto.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendPhoto.getReplyToMessageId());

        if (sendPhoto.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendPhoto.getAllowSendingWithoutReply());

        RequestResponse response = client
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RequestResponse<Message>>() {
                })
                .block();
        return (Message) response.getResult();
    }

    public Message sendRequest(SendAudio sendAudio) throws Exception {
        sendAudio.checkValidation();
        String url = BotInfo.GET_URL() + sendAudio.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendAudio.getChatId());

        inputFile(sendAudio.getAudio(), bodyBuilder, sendAudio.getMethodName());

        if (sendAudio.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendAudio.getMessageThreadId());

        if (sendAudio.getCaption() != null)
            bodyBuilder.part("caption", sendAudio.getCaption());

        bodyBuilder.part("parse_mode", sendAudio.getParseMode());

        if (sendAudio.getParseMode().equals("") && sendAudio.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendAudio.getCaptionEntities());

        if (sendAudio.getDuration() != null)
            bodyBuilder.part("duration", sendAudio.getDuration());

        if (sendAudio.getPerformer() != null)
            bodyBuilder.part("performer", sendAudio.getPerformer());

        if (sendAudio.getTitle() != null)
            bodyBuilder.part("title", sendAudio.getTitle());

        if (sendAudio.getThumbnail() != null)
            inputFile(sendAudio.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendAudio.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendAudio.getDisableNotification());

        if (sendAudio.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendAudio.getProtectContent());

        if (sendAudio.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendAudio.getReplyToMessageId());

        if (sendAudio.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendAudio.getAllowSendingWithoutReply());

        RequestResponse response = client
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<RequestResponse<Message>>() {
                })
                .block();
        return (Message) response.getResult();
    }


    public <Request extends ApiRequest<? extends ReturnObject>, ReturnObject> ReturnObject sendRequest(Request request) throws Exception {
        request.checkValidation();
        String url = BotInfo.GET_URL() + request.getEndPoint();

        RequestResponse response = (RequestResponse) client
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeReferences.get(request.getReturnObject().getClass()))
                .block();
        return (ReturnObject) response.getResult();
    }


    public void inputFile(InputFile inputFile, MultipartBodyBuilder bodyBuilder, String methodName) {
        inputFile.checkValidation(methodName);
        String fileId = inputFile.getFileId();

        if (fileId != null) {
            GetFile getFile = new GetFile(fileId);
            File file = null;
            try {
                file = sendRequest(getFile);
            } catch (Exception e) {
            }
            if (file != null) {
                bodyBuilder.part(methodName, fileId);
                return;
            } else
                throw new IllegalArgumentException("fileId: " + fileId + " does not exists on Telegram server.");
        }

        java.io.File file = inputFile.getFile();
        if (file != null) {
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            bodyBuilder.part(methodName, fileSystemResource);
        }
    }
}
