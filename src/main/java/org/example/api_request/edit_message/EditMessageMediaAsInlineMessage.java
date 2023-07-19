package org.example.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.input_media.InputMedia;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class EditMessageMediaAsInlineMessage implements ApiRequest<Boolean> {
    /**
     * Use this method to edit animation, audio, document, photo, or video messages.
     * If a message is part of a message album, then it can be edited only to an audio for audio albums,
     * only to a document for document albums and to a photo or a video otherwise. When an inline message is edited,
     * a new file can't be uploaded; use a previously uploaded file via its file_id or specify a URL.
     * On success, if the edited message is not an inline message, the edited Message is returned,
     * otherwise True is returned.
     *
     * @param chatId           Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *                         Required if inlineMessageId is not specified.
     * @param messageId        Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId  Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param media            A JSON-serialized object for a new media content of the message.
     * @param replyMarkup      A JSON-serialized object for a new inline keyboard.
     */

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @NonNull
    @JsonProperty("media")
    private InputMedia media;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_MEDIA.getPath();
    }

    @Override
    public void checkValidation() {
        // Additional validation checks can be added as needed.
    }
}
