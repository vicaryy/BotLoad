package org.vicary.api_request.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class GetGameHighScoresAsInlineMessage implements ApiRequest {
    /**
     * Use this method to get data for high score tables.
     *
     * @param userId            Target user id.
     * @param chatId            Unique identifier for the target chat. Required if inlineMessageId is not specified.
     * @param messageId         Identifier of the sent message. Required if inlineMessageId is not specified.
     * @param inlineMessageId   Identifier of the inline message. Required if chatId and messageId are not specified.
     */

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_GAME_HIGH_SCORES.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
