package org.vicary.api_request.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class BanChatMember implements ApiRequest<Boolean> {
    /**
     * Use this method to ban a user in a group, a supergroup, or a channel.
     * In the case of supergroups and channels, the user will not be able to return to the chat on their own
     * using invite links, etc., unless unbanned first.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId            Unique identifier for the target group or username of the target supergroup or channel.
     * @param userId            Unique identifier of the target user.
     * @param untilDate         Date when the user will be unbanned, unix time. If the user is banned for more than
     * 366 days or less than 30 seconds from the current time, they are considered to be banned forever.
     * Applied for supergroups and channels only.
     * @param revokeMessages    Pass True to delete all messages from the chat for the user that is being removed.
     * If False, the user will be able to see messages in the group that were sent before the user was removed.
     * Always True for supergroups and channels.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("until_date")
    private Integer untilDate;

    @JsonProperty("revoke_messages")
    private Boolean revokeMessages;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.BAN_CHAT_MEMBER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}

