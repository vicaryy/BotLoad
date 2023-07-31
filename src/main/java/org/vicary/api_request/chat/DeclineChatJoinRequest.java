package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class DeclineChatJoinRequest implements ApiRequest<Boolean> {
    /**
     * Use this method to decline a chat join request.
     * The bot must be an administrator in the chat for this to work and must have the can_invite_users administrator right.
     *
     * @param chatId  Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param userId  Unique identifier of the target user
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.DECLINE_CHAT_JOIN_REQUEST.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
