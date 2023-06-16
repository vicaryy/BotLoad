package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KeyboardButtonRequestUser implements ApiObject{
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_is_bot")
    private Boolean userIsBot;

    @JsonProperty("user_is_premium")
    private Boolean userIsPremium;

    private KeyboardButtonRequestUser(){}
}
