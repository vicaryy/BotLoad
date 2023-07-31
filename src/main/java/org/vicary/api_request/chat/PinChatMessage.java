package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PinChatMessage implements ApiRequest<Boolean> {
    /**
     * Use this method to add a message to the list of pinned messages in a chat.
     * If the chat is not a private chat, the bot must be an administrator in the chat
     * and must have the 'can_pin_messages' administrator right in a supergroup or 'can_edit_messages' administrator right in a channel.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param messageId           Identifier of a message to pin
     * @param disableNotification Pass True if it is not necessary to send a notification to all chat members about the new pinned message.
     *                            Notifications are always disabled in channels and private chats.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.PIN_CHAT_MESSAGE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
