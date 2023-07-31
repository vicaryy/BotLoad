package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class BanChatSenderChat implements ApiRequest<Boolean> {
    /**
     * Use this method to ban a channel chat in a supergroup or a channel.
     *
     * @param chatId         Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param senderChatId   Unique identifier of the target sender chat
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("sender_chat_id")
    private Integer senderChatId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.BAN_CHAT_SENDER_CHAT.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
