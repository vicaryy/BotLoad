package org.example.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class SetChatStickerSet implements ApiRequest<Boolean> {
    /**
     * Use this method to set a new group sticker set for a supergroup.
     * The bot must be an administrator in the chat for this to work and must have the appropriate administrator rights.
     *
     * @param chatId           Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param stickerSetName   Name of the sticker set to be set as the group sticker set
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("sticker_set_name")
    private String stickerSetName;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_STICKER_SET.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
        if (stickerSetName.isEmpty()) throw new IllegalArgumentException("stickerSetName cannot be empty.");
    }
}
