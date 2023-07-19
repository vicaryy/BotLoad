package org.example.api_request.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_object.menu_button.MenuButton;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetChatMenuButton implements ApiRequest<MenuButton> {
    /**
     * Use this method to get the current value of the bot's menu button in a private chat, or the default menu button.
     *
     * @param chatId Unique identifier for the target private chat. If not specified, the default bot's menu button will be returned.
     */
    @JsonProperty("chat_id")
    private Integer chatId;

    @Override
    public MenuButton getReturnObject() {
        return new MenuButton() {
            @Override
            public String getType() {
                return null;
            }
        };
        //TODO
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_CHAT_MENU_BUTTON.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
