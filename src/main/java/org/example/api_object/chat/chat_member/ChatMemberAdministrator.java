package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberAdministrator implements ChatMember {
    @JsonProperty("status")
    private final String status = "administrator";

    @JsonProperty("user")
    private User user;

    @JsonProperty("can_be_edited")
    private Boolean canBeEdited;

    @JsonProperty("is_anonymous")
    private Boolean isAnonymous;

    @JsonProperty("can_manage_chat")
    private Boolean canManageChat;

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

    @JsonProperty("can_post_messages")
    private Boolean canPostMessages;

    @JsonProperty("can_edit_messages")
    private Boolean canEditMessages;

    @JsonProperty("can_pin_messages")
    private Boolean canPinMessages;

    @JsonProperty("can_manage_topics")
    private Boolean canManageTopics;

    @JsonProperty("custom_title")
    private String customTitle;

    private ChatMemberAdministrator() {
    }
}
