package org.example.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class MessageId implements ApiObject {
    @JsonProperty("message_id")
    private Integer messageId;

    private MessageId() {
    }
}
