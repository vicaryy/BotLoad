package org.example.api_request.games;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendGame implements ApiRequest<Message> {
    /**
     * Use this method to send a game.
     *
     * @param chatId            Unique identifier for the target chat.
     * @param messageThreadId   Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param gameShortName     Short name of the game, serves as the unique identifier for the game. Set up your games via @BotFather.
     * @param disableNotification   Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent    Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId  If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply   Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup       A JSON-serialized object for an inline keyboard. If empty, one 'Play game_title' button will be shown.
     *                          If not empty, the first button must launch the game.
     */

    @NonNull
    @JsonProperty("chat_id")
    private Integer chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("game_short_name")
    private String gameShortName;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_GAME.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
