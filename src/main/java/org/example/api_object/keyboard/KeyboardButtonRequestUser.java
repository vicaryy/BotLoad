package org.example.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class KeyboardButtonRequestUser implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_is_bot")
    private Boolean userIsBot;

    @JsonProperty("user_is_premium")
    private Boolean userIsPremium;

    private KeyboardButtonRequestUser(){}
}
