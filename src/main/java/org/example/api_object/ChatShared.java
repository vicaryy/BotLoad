package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatShared implements ApiObject {
    @JsonProperty("request_id")
    private Integer requestId;

    @JsonProperty("chat_id")
    private Integer chatId;

    private ChatShared() {
    }
}
