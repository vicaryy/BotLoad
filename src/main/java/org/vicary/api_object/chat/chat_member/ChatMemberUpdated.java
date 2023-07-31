package org.vicary.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.chat.Chat;
import org.vicary.api_object.User;
import org.vicary.api_object.chat.ChatInviteLink;

@Getter
@ToString
@EqualsAndHashCode
public class ChatMemberUpdated implements ApiObject {
    @JsonProperty("chat")
    private Chat chat;

    @JsonProperty("from")
    private User from;

    @JsonProperty("date")
    private Integer date;

    @JsonProperty("old_chat_member")
    private ChatMember oldChatMember;

    @JsonProperty("new_chat_member")
    private ChatMember newChatMember;

    @JsonProperty("invite_link")
    private ChatInviteLink inviteLink;

    @JsonProperty("via_chat_folder_invite_link")
    private Boolean viaChatFolderInviteLink;

    private ChatMemberUpdated() {
    }
}
