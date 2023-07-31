package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class DeleteChatPhoto implements ApiRequest<Boolean> {
    /**
     * Use this method to delete a chat photo.
     * Photos can't be changed for private chats.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.DELETE_CHAT_PHOTO.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
