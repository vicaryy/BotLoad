package org.vicary.api_request.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class PromoteChatMember implements ApiRequest<Boolean> {
    /**
     * Use this method to promote or demote a user in a supergroup or a channel.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     * Pass False for all boolean parameters to demote a user.
     *
     * @param chatId                    Unique identifier for the target chat or username of the target channel (in the format @channelusername)
     * @param userId                    Unique identifier of the target user
     * @param isAnonymous               Pass True if the administrator's presence in the chat is hidden
     * @param canManageChat             Pass True if the administrator can access the chat event log, chat statistics,
     *                                  message statistics in channels, see channel members,
     *                                  see anonymous administrators in supergroups and ignore slow mode.
     *                                  Implied by any other administrator privilege
     * @param canPostMessages           Pass True if the administrator can create channel posts, channels only
     * @param canEditMessages           Pass True if the administrator can edit messages of other users and can pin messages, channels only
     * @param canDeleteMessages         Pass True if the administrator can delete messages of other users
     * @param canManageVideoChats       Pass True if the administrator can manage video chats
     * @param canRestrictMembers        Pass True if the administrator can restrict, ban or unban chat members
     * @param canPromoteMembers         Pass True if the administrator can add new administrators with a subset of their own privileges
     *                                  or demote administrators that they have promoted, directly or indirectly
     *                                  (promoted by administrators that were appointed by him)
     * @param canChangeInfo             Pass True if the administrator can change chat title, photo and other settings
     * @param canInviteUsers            Pass True if the administrator can invite new users to the chat
     * @param canPinMessages            Pass True if the administrator can pin messages, supergroups only
     * @param canManageTopics           Pass True if the user is allowed to create, rename, close, and reopen forum topics, supergroups only
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("can_manage_chat")
    private Boolean canManageChat;

    @JsonProperty("can_post_messages")
    private Boolean canPostMessages;

    @JsonProperty("can_edit_messages")
    private Boolean canEditMessages;

    @JsonProperty("can_delete_messages")
    private Boolean canDeleteMessages;

    @JsonProperty("can_manage_video_chats")
    private Boolean canManageVideoChats;

    @JsonProperty("can_restrict_members")
    private Boolean canRestrictMembers;

    @JsonProperty("can_promote_members")
    private Boolean canPromoteMembers;

    @JsonProperty("can_change_info")
    private Boolean canChangeInfo;

    @JsonProperty("can_invite_users")
    private Boolean canInviteUsers;

    @JsonProperty("can_pin_messages")
    private Boolean canPinMessages;

    @JsonProperty("can_manage_topics")
    private Boolean canManageTopics;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.PROMOTE_CHAT_MEMBER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
