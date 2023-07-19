package org.example.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.MessageEntity;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EditMessageTextAsInlineMessage implements ApiRequest<Boolean> {
    /**
     * Use this method to edit text and game messages.
     *
     * @param chatId             Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *                           Required if inlineMessageId is not specified.
     * @param messageId          Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId    Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param text               New text of the message, 1-4096 characters after entities parsing.
     * @param parseMode          Mode for parsing entities in the message text. See formatting options for more details.
     * @param entities           A JSON-serialized list of special entities that appear in the message text, which can be specified instead of parseMode.
     * @param disableWebPagePreview   Disables link previews for links in this message.
     * @param replyMarkup        A JSON-serialized object for an inline keyboard.
     */

    @NonNull
    @JsonProperty("inline_message_id")
    private String inlineMessageId;

    @NonNull
    @JsonProperty("text")
    private String text;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    public void setParseModeOnMarkdownV2() {
        parseMode = "MarkdownV2";
    }
    public void setParseModeOnMarkdown() {
        parseMode = "Markdown";
    }
    public void setParseModeOnHTML() {
        parseMode = "HTML";
    }

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_TEXT.getPath();
    }

    @Override
    public void checkValidation() {
        if(text.isEmpty()) throw new IllegalArgumentException("text cannot be empty.");

        if (parseMode == null)
            parseMode = "";

        if (!parseMode.equals("HTML") && !parseMode.equals("MarkdownV2") && !parseMode.equals("Markdown") && !parseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + parseMode + "\" does not exist.");

        if (!parseMode.equals("") && (entities != null && !entities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");
    }
}
