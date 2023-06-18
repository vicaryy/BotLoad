package org.example.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class MessageAutoDeleteTimerChanged implements ApiObject {
    @JsonProperty("message_auto_delete_time")
    private Integer messageAutoDeleteTime;

    private MessageAutoDeleteTimerChanged() {
    }
}
