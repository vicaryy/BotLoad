package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CallbackQuery implements ApiObject {
    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private User from;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @JsonProperty("chat_instance")
    private String chatInstance;

    @JsonProperty("data")
    private String data;

    @JsonProperty("game_short_name")
    private String gameShortName;

    private CallbackQuery() {
    }
}
