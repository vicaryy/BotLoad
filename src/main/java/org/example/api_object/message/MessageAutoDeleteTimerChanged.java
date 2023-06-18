package org.example.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class MessageAutoDeleteTimerChanged implements ApiObject {
    @JsonProperty("message_auto_delete_time")
    private Integer messageAutoDeleteTime;

    private MessageAutoDeleteTimerChanged() {
    }
}
