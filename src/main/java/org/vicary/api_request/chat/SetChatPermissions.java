package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.chat.ChatPermissions;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SetChatPermissions implements ApiRequest<Boolean> {
    /**
     * Use this method to set default chat permissions for all members in a group or a supergroup.
     *
     * @param chatId                        Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param permissions                   A JSON-serialized object for new default chat permissions
     * @param useIndependentChatPermissions Pass True if chat permissions are set independently. Otherwise,
     * the can_send_other_messages and can_add_web_page_previews permissions
     * will imply the can_send_messages, can_send_audios, can_send_documents,
     * can_send_photos, can_send_videos, can_send_video_notes, and can_send_voice_notes permissions;
     * the can_send_polls permission will imply the can_send_messages permission.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("permissions")
    private ChatPermissions permissions;

    @JsonProperty("use_independent_chat_permissions")
    private Boolean useIndependentChatPermissions;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_PERMISSIONS.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
