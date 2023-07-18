package org.example.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.input_media.InputMedia;
import org.example.api_object.message.Message;
import org.example.api_request.ApiRequestList;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendMediaGroup implements ApiRequestList<Message> {
    /**
     * Use this method to send a group of photos, videos, documents, or audios as an album.
     * Documents and audio files can only be grouped in an album with messages of the same type.
     * On success, an array of Messages that were sent is returned.
     *
     * @param chatId                  Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId         Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param media                   A JSON-serialized array describing messages to be sent, must include 2-10 items.
     * @param disableNotification     Optional. Sends messages silently. Users will receive a notification with no sound.
     * @param protectContent          Optional. Protects the contents of the sent messages from forwarding and saving.
     * @param replyToMessageId        Optional. If the messages are a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("media")
    private List<InputMedia> media;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_MEDIA_GROUP.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}

