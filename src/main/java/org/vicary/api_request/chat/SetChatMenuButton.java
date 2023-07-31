package org.vicary.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.menu_button.MenuButton;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetChatMenuButton implements ApiRequest<Boolean> {
    /**
     * Use this method to change the bot's menu button in a private chat, or the default menu button.
     *
     * @param chatId     Unique identifier for the target private chat. If not specified, the default bot's menu button will be changed.
     * @param menuButton A JSON-serialized object for the bot's new menu button. Defaults to MenuButtonDefault.
     */
    @JsonProperty("chat_id")
    private Integer chatId;

    @JsonProperty("menu_button")
    private MenuButton menuButton;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_CHAT_MENU_BUTTON.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
