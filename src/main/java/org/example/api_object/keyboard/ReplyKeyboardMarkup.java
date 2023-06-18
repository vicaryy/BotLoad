package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.keyboard.KeyboardButton;

@Getter
public class ReplyKeyboardMarkup implements ApiObject {
    @JsonProperty("keyboard")
    private List<List<KeyboardButton>> keyboard;

    @JsonProperty("is_persistent")
    private Boolean isPersistent;

    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard;

    @JsonProperty("one_time_keyboard")
    private Boolean oneTimeKeyboard;

    @JsonProperty("input_field_placeholder")
    private String inputFieldPlaceholder;

    @JsonProperty("selective")
    private Boolean selective;

    private ReplyKeyboardMarkup(){}
}
