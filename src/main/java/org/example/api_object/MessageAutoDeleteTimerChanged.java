package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageAutoDeleteTimerChanged implements ApiObject {
    @JsonProperty("message_auto_delete_time")
    private Integer messageAutoDeleteTime;

    private MessageAutoDeleteTimerChanged() {
    }
}
