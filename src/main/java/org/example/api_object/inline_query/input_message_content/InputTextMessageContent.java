package org.example.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.message.MessageEntity;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InputTextMessageContent implements InputMessageContent {
    @JsonProperty("message_text")
    private String messageText;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("disable_web_page_preview")
    private boolean disableWebPagePreview;

    private InputTextMessageContent() {
    }
}
