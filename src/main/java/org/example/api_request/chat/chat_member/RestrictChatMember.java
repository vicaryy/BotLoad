package org.example.api_request.chat.chat_member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.chat.ChatPermissions;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RestrictChatMember implements ApiRequest<Boolean> {
    /**
     * Use this method to restrict a user in a supergroup.
     * The bot must be an administrator in the supergroup for this to work and must have the appropriate administrator rights.
     * Pass True for all permissions to lift restrictions from a user.
     *
     * @param chatId                  Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param userId                  Unique identifier of the target user
     * @param permissions             A JSON-serialized object for new user permissions
     * @param useIndependentChatPermissions Pass True if chat permissions are set independently.
     *                                     Otherwise, the can_send_other_messages and can_add_web_page_previews permissions will imply the can_send_messages,
     *                                     can_send_audios, can_send_documents, can_send_photos, can_send_videos, can_send_video_notes,
     *                                     and can_send_voice_notes permissions;
     *                                     the can_send_polls permission will imply the can_send_messages permission.
     * @param untilDate               Date when restrictions will be lifted for the user, unix time.
     *                                If the user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("permissions")
    private ChatPermissions permissions;

    @JsonProperty("use_independent_chat_permissions")
    private Boolean useIndependentChatPermissions;

    @JsonProperty("until_date")
    private Integer untilDate;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.RESTRICT_CHAT_MEMBER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
