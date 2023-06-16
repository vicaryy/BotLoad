package org.example.api_object.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.User;

@Getter
public class ChatMemberRestricted implements ChatMember {
    @JsonProperty("status")
    private final String status = "restricted";

    @JsonProperty("user")
    private User user;

    @JsonProperty("is_member")
    private Boolean isMember;

    @JsonProperty("can_send_messages")
    private Boolean canSendMessages;

    @JsonProperty("can_send_audios")
    private Boolean canSendAudios;

    @JsonProperty("can_send_documents")
    private Boolean canSendDocuments;

    @JsonProperty("can_send_photos")
    private Boolean canSendPhotos;

    @JsonProperty("can_send_videos")
    private Boolean canSendVideos;

    @JsonProperty("can_send_video_notes")
    private Boolean canSendVideoNotes;

    @JsonProperty("can_send_voice_notes")
    private Boolean canSendVoiceNotes;

    @JsonProperty("can_send_polls")
    private Boolean canSendPolls;

    @JsonProperty("can_send_other_messages")
    private Boolean canSendOtherMessages;

    @JsonProperty("can_add_web_page_previews")
    private Boolean canAddWebPagePreviews;

    @JsonProperty("can_change_info")
    private Boolean canChangeInfo;

    @JsonProperty("can_invite_users")
    private Boolean canInviteUsers;

    @JsonProperty("can_pin_messages")
    private Boolean canPinMessages;

    @JsonProperty("can_manage_topics")
    private Boolean canManageTopics;

    @JsonProperty("until_date")
    private Integer untilDate;

    private ChatMemberRestricted() {
    }
}
