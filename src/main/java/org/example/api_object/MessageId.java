package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MessageId implements ApiObject{
    @JsonProperty("message_id")
    private Integer messageId;

    private MessageId() {
    }
}
