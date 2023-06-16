package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ReplyKeyboardRemove implements ApiObject {
    @JsonProperty("remove_keyboard")
    private Boolean removeKeyboard;

    @JsonProperty("selective")
    private Boolean selective;

    private ReplyKeyboardRemove() {
    }
}
