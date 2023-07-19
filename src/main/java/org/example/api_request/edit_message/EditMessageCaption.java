package org.example.api_request.edit_message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_object.message.MessageEntity;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class EditMessageCaption implements ApiRequest<Message> {
    /**
     * Use this method to edit captions of messages.
     *
     * @param chatId           Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     *                         Required if inlineMessageId is not specified.
     * @param messageId        Identifier of the message to edit. Required if inlineMessageId is not specified.
     * @param inlineMessageId  Identifier of the inline message. Required if chatId and messageId are not specified.
     * @param caption          New caption of the message, 0-1024 characters after entities parsing.
     * @param parseMode        Mode for parsing entities in the message caption. See formatting options for more details.
     * @param captionEntities  A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parseMode.
     * @param replyMarkup      A JSON-serialized object for an inline keyboard.
     */
    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

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
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.EDIT_MESSAGE_CAPTION.getPath();
    }

    @Override
    public void checkValidation() {
        if(chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");

        if (parseMode == null)
            parseMode = "";

        if (!parseMode.equals("HTML") && !parseMode.equals("MarkdownV2") && !parseMode.equals("Markdown") && !parseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + parseMode + "\" does not exist.");

        if (!parseMode.equals("") && (captionEntities != null && !captionEntities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");
    }
}

