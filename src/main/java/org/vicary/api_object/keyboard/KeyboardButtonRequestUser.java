package org.vicary.api_object.keyboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class KeyboardButtonRequestUser implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("user_is_bot")
    private Boolean userIsBot;

    @JsonProperty("user_is_premium")
    private Boolean userIsPremium;

    private KeyboardButtonRequestUser(){}
}
