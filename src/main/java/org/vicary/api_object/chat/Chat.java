package org.vicary.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.message.Message;

@Data
public class Chat implements ApiObject {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("title")
    private String title;

    @JsonProperty("username")
    private String username;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("is_forum")
    private Boolean isForum;

    @JsonProperty("photo")
    private ChatPhoto photo;

//    @JsonProperty("active_usernames")
//    private List<String> activeUsernames;

    @JsonProperty("emoji_status_custom_emoji_id")
    private String emojiStatusCustomEmojiId;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("has_private_forwards")
    private Boolean hasPrivateForwards;

    @JsonProperty("has_restricted_voice_and_video_messages")
    private Boolean hasRestrictedVoiceAndVideoMessages;

    @JsonProperty("join_to_send_messages")
    private Boolean joinToSendMessages;

    @JsonProperty("join_by_request")
    private Boolean joinByRequest;

    @JsonProperty("description")
    private String description;

    @JsonProperty("invite_link")
    private String inviteLink;

    @JsonProperty("pinned_message")
    private Message pinnedMessage;

    @JsonProperty("permissions")
    private ChatPermissions permissions;

    @JsonProperty("slow_delay_mode")
    private Integer slowDelayMode;

    @JsonProperty("message_auto_delete_time")
    private Integer messageAutoDeleteTime;

    @JsonProperty("has_aggressive_anti_spam_enabled")
    private Boolean hasAggressiveAntiSpamEnabled;

    @JsonProperty("has_hidden_members")
    private Boolean hasHiddenMembers;

    @JsonProperty("has_protected_content")
    private Boolean hasProtectedContent;

    @JsonProperty("sticker_set_name")
    private String stickerSetName;

    @JsonProperty("can_set_sticker_set")
    private Boolean canSetStickerSet;

    @JsonProperty("linked_chat_id")
    private Integer linkedChatId;

    @JsonProperty("location")
    private ChatLocation location;
}
