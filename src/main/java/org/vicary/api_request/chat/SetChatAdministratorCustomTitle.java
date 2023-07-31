package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class SetChatAdministratorCustomTitle implements ApiRequest<Boolean> {
    /**
     * Use this method to set a custom title for an administrator in a supergroup promoted by the bot.
     *
     * @param chatId       Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param userId       Unique identifier of the target user
     * @param customTitle  New custom title for the administrator; 0-16 characters, emoji are not allowed
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("custom_title")
    private String customTitle;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_ADMINISTRATOR_CUSTOM_TITLE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
        if (customTitle.isEmpty()) throw new IllegalArgumentException("customTitle cannot be empty.");
        if (customTitle.length() > 16) throw new IllegalArgumentException("customTitle length should be between 0 and 16 characters.");
    }
}
