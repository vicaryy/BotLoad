package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SetChatDescription implements ApiRequest<Boolean> {
    /**
     * Use this method to change the description of a group, supergroup, or channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId      Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param description New chat description, 0-255 characters
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("description")
    private String description;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_DESCRIPTION.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
