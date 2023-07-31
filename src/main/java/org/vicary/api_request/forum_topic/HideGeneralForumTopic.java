package org.vicary.api_request.forum_topic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class HideGeneralForumTopic implements ApiRequest<Boolean> {
    /**
     * Use this method to hide the 'General' topic in a forum supergroup chat.
     * The bot must be an administrator in the chat for this to work and must have can_manage_topics administrator rights.
     * The topic will be automatically closed if it was open.
     *
     * @param chatId Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.HIDE_GENERAL_FORUM_TOPIC.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
