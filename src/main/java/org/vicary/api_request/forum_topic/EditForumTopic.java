package org.vicary.api_request.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EditForumTopic implements ApiRequest<Boolean> {
    /**
     * Use this method to edit the name and icon of a topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights,
     * unless it is the creator of the topic.
     *
     * @param chatId           Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     * @param messageThreadId  Unique identifier for the target message thread of the forum topic
     * @param name             New topic name, 0-128 characters. If not specified or empty, the current name of the topic will be kept
     * @param iconCustomEmojiId New unique identifier of the custom emoji shown as the topic icon.
     * Use getForumTopicIconStickers to get all allowed custom emoji identifiers.
     * Pass an empty string to remove the icon. If not specified, the current icon will be kept
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("icon_custom_emoji_id")
    private String iconCustomEmojiId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_FORUM_TOPIC.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
