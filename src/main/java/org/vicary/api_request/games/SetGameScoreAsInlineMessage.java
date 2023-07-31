package org.vicary.api_request.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SetGameScoreAsInlineMessage implements ApiRequest<Boolean> {
    /**
     * Use this method to set the score of the specified user in a game message.
     * On success, if the message is not an inline message, the Message is returned,
     * otherwise True is returned. Returns an error, if the new score is not greater than the user's
     * current score in the chat and force is False.
     *
     * @param userId            User identifier.
     * @param score             New score, must be non-negative.
     * @param force             Pass True if the high score is allowed to decrease. This can be useful when fixing mistakes or banning cheaters.
     * @param disableEditMessage    Pass True if the game message should not be automatically edited to include the current scoreboard.
     * @param chatId            Unique identifier for the target chat. Required if inlineMessageId is not specified.
     * @param messageId         Identifier of the sent message. Required if inlineMessageId is not specified.
     * @param inlineMessageId   Identifier of the inline message. Required if chatId and messageId are not specified.
     */

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("score")
    private Integer score;

    @JsonProperty("force")
    private Boolean force;

    @JsonProperty("disable_edit_message")
    private Boolean disableEditMessage;

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_GAME_SCORE.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
