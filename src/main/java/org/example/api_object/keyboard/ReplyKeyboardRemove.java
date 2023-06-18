package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class ReplyKeyboardRemove implements ApiObject {
    @JsonProperty("remove_keyboard")
    private Boolean removeKeyboard;

    @JsonProperty("selective")
    private Boolean selective;

    private ReplyKeyboardRemove() {
    }
}
