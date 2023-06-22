package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_object.message.MessageEntity;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessage implements ApiRequest<Message> {
    /**
     * Use this method to send text messages. On success, the sent Message is returned.
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param text                   Text of the message to be sent, 1-4096 characters after entities parsing
     * @param parseMode              Mode for parsing entities in the message text. See formatting options for more details.
     * @param entities               A JSON-serialized list of special entities that appear in message text, which can be specified instead of parse_mode.
     * @param disableWebPagePreview  Disables link previews for links in this message
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the sent message from forwarding and saving
     * @param replyToMessageId       If the message is a reply, ID of the original message
     * @param allowSendingWithoutReply Pass True if the message should be sent even if the specified replied-to message is not found
     * @param replyMarkup            Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("text")
    private String text;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("entities")
    private List<MessageEntity> entities;

    @JsonProperty("disable_web_page_preview")
    private Boolean disableWebPagePreview;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;  // Does not work properly

    public void setParseModeOnMarkdownV2() {
        parseMode = "MarkdownV2";
    }

    public void setParseModeOnHTML() {
        parseMode = "HTML";
    }

    @Override
    public void checkValidation() {
        if (parseMode == null)
            parseMode = "";

        if (!parseMode.equals("HTML") && !parseMode.equals("MarkdownV2") && !parseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + parseMode + "\" does not exist.");

        if (!parseMode.equals("") && (entities != null && !entities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");
    }
    @Override
    public Message getReturnObject() {
        return new Message();
    }
    @Override
    public String getEndPoint() {
        return EndPoint.SEND_MESSAGE.getPath();
    }
}
