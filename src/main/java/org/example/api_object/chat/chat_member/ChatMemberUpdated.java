package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.Chat;
import org.example.api_object.User;
import org.example.api_object.chat.ChatInviteLink;

@Getter
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
