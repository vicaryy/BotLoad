package org.vicary.api_object.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.User;
import org.vicary.api_object.message.Message;

@Getter
@ToString
@EqualsAndHashCode
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
