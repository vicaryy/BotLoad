package org.example.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.poll.Poll;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class StopPoll implements ApiRequest<Poll> {
    /**
     * Use this method to stop a poll which was sent by the bot.
     *
     * @param chatId       Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageId    Identifier of the original message with the poll.
     * @param replyMarkup  A JSON-serialized object for a new message inline keyboard.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Poll getReturnObject() {
        return new Poll();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.STOP_POLL.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
