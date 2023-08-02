package org.vicary.service;

import org.vicary.api_object.File;
import org.vicary.api_object.RequestResponse;
import org.vicary.api_object.RequestResponseList;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.*;
import org.vicary.api_request.send.*;
import org.vicary.configuration.BotInfo;
import org.vicary.configuration.ParameterizedTypeReferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.NoSuchElementException;

@Service
public class RequestService {
    private final WebClient client;
    private final ParameterizedTypeReferences typeReferences;

    @Autowired
    public RequestService(WebClient client,
                          ParameterizedTypeReferences typeReferences) {
        this.client = client;
        this.typeReferences = typeReferences;
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

    public <Request extends ApiRequestList<? extends ReturnObject>, ReturnObject> ReturnObject sendRequest(Request request) throws Exception {
        request.checkValidation();
        String url = BotInfo.GET_URL() + request.getEndPoint();

        RequestResponseList response = (RequestResponseList) client
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeReferences.get(request.getReturnObject().getClass()))
                .block();
        return (ReturnObject) response.getResult();
    }

    public <Request extends ApiRequest> void sendRequestAsync(Request request) throws Exception{
        request.checkValidation();
        String url = BotInfo.GET_URL() + request.getEndPoint();

        client
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
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

        return sendRequest(url, bodyBuilder);
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

        return sendRequest(url, bodyBuilder);
    }

    public Message sendRequest(SendDocument sendDocument) throws Exception {
        sendDocument.checkValidation();
        String url = BotInfo.GET_URL() + sendDocument.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendDocument.getChatId());

        if (sendDocument.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendDocument.getMessageThreadId());

        inputFile(sendDocument.getDocument(), bodyBuilder, sendDocument.getMethodName());

        if (sendDocument.getThumbnail() != null)
            inputFile(sendDocument.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendDocument.getCaption() != null)
            bodyBuilder.part("caption", sendDocument.getCaption());

        bodyBuilder.part("parse_mode", sendDocument.getParseMode());

        if (sendDocument.getParseMode().equals("") && sendDocument.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendDocument.getCaptionEntities());

        if (sendDocument.getDisableContentTypeDetection() != null)
            bodyBuilder.part("disable_content_type_detection", sendDocument.getDisableContentTypeDetection());

        if (sendDocument.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendDocument.getDisableNotification());

        if (sendDocument.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendDocument.getProtectContent());

        if (sendDocument.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendDocument.getReplyToMessageId());

        if (sendDocument.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendDocument.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }

    public Message sendRequest(SendVideo sendVideo) throws Exception {
        sendVideo.checkValidation();
        String url = BotInfo.GET_URL() + sendVideo.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVideo.getChatId());

        if (sendVideo.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVideo.getMessageThreadId());

        inputFile(sendVideo.getVideo(), bodyBuilder, sendVideo.getMethodName());

        if (sendVideo.getDuration() != null)
            bodyBuilder.part("duration", sendVideo.getDuration());

        if (sendVideo.getWidth() != null)
            bodyBuilder.part("width", sendVideo.getWidth());

        if (sendVideo.getHeight() != null)
            bodyBuilder.part("height", sendVideo.getHeight());

        if (sendVideo.getThumbnail() != null)
            inputFile(sendVideo.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendVideo.getCaption() != null)
            bodyBuilder.part("caption", sendVideo.getCaption());

        bodyBuilder.part("parse_mode", sendVideo.getParseMode());

        if (sendVideo.getParseMode().equals("") && sendVideo.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendVideo.getCaptionEntities());

        if (sendVideo.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendVideo.getHasSpoiler());

        if (sendVideo.getSupportsStreaming() != null)
            bodyBuilder.part("supports_streaming", sendVideo.getSupportsStreaming());

        if (sendVideo.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVideo.getDisableNotification());

        if (sendVideo.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVideo.getProtectContent());

        if (sendVideo.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVideo.getReplyToMessageId());

        if (sendVideo.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVideo.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }

    public Message sendRequest(SendAnimation sendAnimation) throws Exception {
        sendAnimation.checkValidation();
        String url = BotInfo.GET_URL() + sendAnimation.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendAnimation.getChatId());

        if (sendAnimation.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendAnimation.getMessageThreadId());

        inputFile(sendAnimation.getAnimation(), bodyBuilder, sendAnimation.getMethodName());

        if (sendAnimation.getDuration() != null)
            bodyBuilder.part("duration", sendAnimation.getDuration());

        if (sendAnimation.getWidth() != null)
            bodyBuilder.part("width", sendAnimation.getWidth());

        if (sendAnimation.getHeight() != null)
            bodyBuilder.part("height", sendAnimation.getHeight());

        if (sendAnimation.getThumbnail() != null)
            inputFile(sendAnimation.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendAnimation.getCaption() != null)
            bodyBuilder.part("caption", sendAnimation.getCaption());

        bodyBuilder.part("parse_mode", sendAnimation.getParseMode());

        if (sendAnimation.getParseMode().equals("") && sendAnimation.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendAnimation.getCaptionEntities());

        if (sendAnimation.getHasSpoiler() != null)
            bodyBuilder.part("has_spoiler", sendAnimation.getHasSpoiler());

        if (sendAnimation.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendAnimation.getDisableNotification());

        if (sendAnimation.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendAnimation.getProtectContent());

        if (sendAnimation.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendAnimation.getReplyToMessageId());

        if (sendAnimation.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendAnimation.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }

    public Message sendRequest(SendVoice sendVoice) throws Exception {
        sendVoice.checkValidation();
        String url = BotInfo.GET_URL() + sendVoice.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVoice.getChatId());

        if (sendVoice.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVoice.getMessageThreadId());

        inputFile(sendVoice.getVoice(), bodyBuilder, sendVoice.getMethodName());

        if (sendVoice.getCaption() != null)
            bodyBuilder.part("caption", sendVoice.getCaption());

        bodyBuilder.part("parse_mode", sendVoice.getParseMode());

        if (sendVoice.getParseMode().equals("") && sendVoice.getCaptionEntities() != null)
            bodyBuilder.part("caption_entities", sendVoice.getCaptionEntities());

        if (sendVoice.getDuration() != null)
            bodyBuilder.part("duration", sendVoice.getDuration());

        if (sendVoice.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVoice.getDisableNotification());

        if (sendVoice.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVoice.getProtectContent());

        if (sendVoice.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVoice.getReplyToMessageId());

        if (sendVoice.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVoice.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }

    public Message sendRequest(SendVideoNote sendVideoNote) throws Exception {
        sendVideoNote.checkValidation();
        String url = BotInfo.GET_URL() + sendVideoNote.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendVideoNote.getChatId());

        if (sendVideoNote.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendVideoNote.getMessageThreadId());

        inputFile(sendVideoNote.getVideoNote(), bodyBuilder, sendVideoNote.getMethodName());

        if (sendVideoNote.getDuration() != null)
            bodyBuilder.part("duration", sendVideoNote.getDuration());

        if (sendVideoNote.getLength() != null)
            bodyBuilder.part("length", sendVideoNote.getLength());

        if (sendVideoNote.getThumbnail() != null)
            inputFile(sendVideoNote.getThumbnail(), bodyBuilder, "thumbnail");

        if (sendVideoNote.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendVideoNote.getDisableNotification());

        if (sendVideoNote.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendVideoNote.getProtectContent());

        if (sendVideoNote.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendVideoNote.getReplyToMessageId());

        if (sendVideoNote.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendVideoNote.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }

    public Message sendSticker(SendSticker sendSticker) throws Exception {
        sendSticker.checkValidation();
        String url = BotInfo.GET_URL() + sendSticker.getEndPoint();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        bodyBuilder.part("chat_id", sendSticker.getChatId());

        if (sendSticker.getMessageThreadId() != null)
            bodyBuilder.part("message_thread_id", sendSticker.getMessageThreadId());

        inputFile(sendSticker.getSticker(), bodyBuilder, sendSticker.getMethodName());

        if (sendSticker.getEmoji() != null)
            bodyBuilder.part("duration", sendSticker.getEmoji());

        if (sendSticker.getDisableNotification() != null)
            bodyBuilder.part("disable_notification", sendSticker.getDisableNotification());

        if (sendSticker.getProtectContent() != null)
            bodyBuilder.part("protect_content", sendSticker.getProtectContent());

        if (sendSticker.getReplyToMessageId() != null)
            bodyBuilder.part("reply_to_message_id", sendSticker.getReplyToMessageId());

        if (sendSticker.getAllowSendingWithoutReply() != null)
            bodyBuilder.part("allow_sending_without_reply", sendSticker.getAllowSendingWithoutReply());

        return sendRequest(url, bodyBuilder);
    }


    private Message sendRequest(String url, MultipartBodyBuilder bodyBuilder) {
        RequestResponse response = (RequestResponse) client
                .post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(typeReferences.get(Message.class))
                .block();
        return (Message) response.getResult();
    }

    private void inputFile(InputFile inputFile, MultipartBodyBuilder bodyBuilder, String methodName) {
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

        if (inputFile.getFile() == null)
            throw new NoSuchElementException("File cannot be null if fileId does not exists on Telegram server.");

        if (!inputFile.getFile().exists())
            throw new NoSuchElementException("File does not exist. \nFile path: " + inputFile.getFile().getPath());

        java.io.File file = inputFile.getFile();
        if (file != null) {
            FileSystemResource fileSystemResource = new FileSystemResource(file);
            bodyBuilder.part(methodName, fileSystemResource);
        }
    }
}
