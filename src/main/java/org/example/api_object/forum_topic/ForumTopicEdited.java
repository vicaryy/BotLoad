package org.example.api_object.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class ForumTopicEdited implements ApiObject {
    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    private ForumTopicEdited() {
    }
}
