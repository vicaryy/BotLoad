package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class InlineKeyboardMarkup implements ApiObject {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;

    private InlineKeyboardMarkup() {
    }
}
