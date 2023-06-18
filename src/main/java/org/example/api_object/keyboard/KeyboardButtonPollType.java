package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class KeyboardButtonPollType implements ApiObject {
    @JsonProperty("type")
    private String type;

    private KeyboardButtonPollType() {
    }
}
