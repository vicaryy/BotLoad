package org.example.api_request.chat.chat_invite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.chat.ChatInviteLink;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RevokeChatInviteLink implements ApiRequest<ChatInviteLink> {
    /**
     * Use this method to revoke an invite link created by the bot.
     * If the primary link is revoked, a new link is automatically generated.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId      Unique identifier of the target chat or username of the target channel (in the format @channelusername)
     * @param inviteLink  The invite link to revoke
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("invite_link")
    private String inviteLink;

    @Override
    public ChatInviteLink getReturnObject() {
        return new ChatInviteLink();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.REVOKE_CHAT_INVITE_LINK.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
        if (inviteLink.isEmpty()) throw new IllegalArgumentException("inviteLink cannot be empty.");
    }
}
