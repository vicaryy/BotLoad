package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InlineKeyboardMarkup implements ApiObject {
    @JsonProperty("inline_keyboard")
    private List<List<InlineKeyboardButton>> inlineKeyboard;

    private InlineKeyboardMarkup() {
    }
}
