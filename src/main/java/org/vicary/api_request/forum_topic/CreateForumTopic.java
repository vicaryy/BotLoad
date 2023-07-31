package org.vicary.api_request.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.forum_topic.ForumTopic;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CreateForumTopic implements ApiRequest<ForumTopic> {
    /**
     * Use this method to create a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have the can_manage_topics administrator rights.
     *
     * @param chatId            Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param name              Topic name, 1-128 characters
     * @param iconColor         Color of the topic icon in RGB format. Currently, must be one of 7322096 (0x6FB9F0),
     * 16766590 (0xFFD67E), 13338331 (0xCB86DB), 9367192 (0x8EEE98), 16749490 (0xFF93B2), or 16478047 (0xFB6F5F)
     * @param iconCustomEmojiId Unique identifier of the custom emoji shown as the topic icon.
     * Use getForumTopicIconStickers to get all allowed custom emoji identifiers.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_color")
    private Integer iconColor;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    @Override
    public ForumTopic getReturnObject() {
        return new ForumTopic();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.CREATE_FORUM_TOPIC.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
