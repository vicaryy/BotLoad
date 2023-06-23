package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ForwardMessage implements ApiRequest<Message> {
    /**
     * Use this method to forward messages of any kind. Service messages can't be forwarded.
     * On success, the sent Message is returned.
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param fromChatId             Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername).
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the forwarded message from forwarding and saving.
     * @param messageId              Message identifier in the chat specified in fromChatId.
     * @return Message class.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("from_chat_id")
    private String fromChatId;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.FORWARD_MESSAGE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
        if (fromChatId.isEmpty()) throw new IllegalArgumentException("fromChatId cannot be empty.");
    }
}
