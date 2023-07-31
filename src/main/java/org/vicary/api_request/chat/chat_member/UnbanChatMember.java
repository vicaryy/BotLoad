package org.vicary.api_request.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UnbanChatMember implements ApiRequest<Boolean> {
    /**
     * Use this method to unban a previously banned user in a supergroup or channel.
     * The user will not return to the group or channel automatically, but will be able to join via link, etc.
     * The bot must be an administrator for this to work. By default, this method guarantees that after the call,
     * the user is not a member of the chat, but will be able to join it.
     * So if the user is a member of the chat, they will also be removed from the chat.
     * If you don't want this, use the parameter only_if_banned.
     *
     * @param chatId        Unique identifier for the target group or username of the target supergroup or channel.
     * @param userId        Unique identifier of the target user.
     * @param onlyIfBanned  Do nothing if the user is not banned.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("only_if_banned")
    private Boolean onlyIfBanned;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.UNBAN_CHAT_MEMBER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
