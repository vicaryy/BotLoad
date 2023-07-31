package org.vicary.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.message.Message;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class EditMessageReplyMarkup implements ApiRequest<Message> {
    /**
     * Use this method to edit only the reply markup of messages.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *                            Required if inlineMessageId is not specified.
     * @param messageId           Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId     Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param replyMarkup         A JSON-serialized object for an inline keyboard.
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
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_REPLY_MARKUP.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
