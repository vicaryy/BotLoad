package org.example.api_object.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.User;

@Getter
public class MessageEntity implements ApiObject {
    @JsonProperty("type")
    private String type;

    @JsonProperty("offset")
    private Integer offset;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("url")
    private String url;

    @JsonProperty("user")
    private User user;

    @JsonProperty("language")
    private String language;

    @JsonProperty("custom_emoji_id")
    private String customEmojiId;

    private MessageEntity() {
    }

}
