package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.MessageEntity;
import org.example.api_object.message.MessageId;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CopyMessage implements ApiRequest<MessageId> {
    /**
     * Use this method to copy messages of any kind. Service messages and invoice messages can't be copied.
     * A quiz poll can be copied only if the value of the field correct_option_id is known to the bot.
     * The method is analogous to the method forwardMessage, but the copied message doesn't have a link to the original message.
     * Returns the MessageId of the sent message on success. Expand to see more...
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param fromChatId             Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername).
     * @param messageId              Message identifier in the chat specified in fromChatId.
     * @param caption                New caption for media, 0-1024 characters after entities parsing. If not specified, the original caption is kept.
     * @param parseMode              Mode for parsing entities in the new caption. See formatting options for more details.
     * @param captionEntities        A JSON-serialized list of special entities that appear in the new caption, which can be specified instead of parseMode.
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId       If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup            Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *                               instructions to remove reply keyboard or to force a reply from the user.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("from_chat_id")
    private String fromChatId;

    @NonNull
    @JsonProperty("message_id")
    private Integer messageId;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")             // Does not work properly.
//    private InlineKeyboardMarkup replyMarkup;

    public void setParseModeOnMarkdownV2() {
        parseMode = "MarkdownV2";
    }

    public void setParseModeOnHTML() {
        parseMode = "HTML";
    }

    @Override
    public MessageId getReturnObject() {
        return null;
    }

    public String getEndPoint() {
        return EndPoint.COPY_MESSAGE.getPath();
    }

    @Override
    public void checkValidation() {
        if(chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
        if(fromChatId.isEmpty()) throw new IllegalArgumentException("fromChatId cannot be empty.");

        if (parseMode == null)
            parseMode = "";

        if (!parseMode.equals("HTML") && !parseMode.equals("MarkdownV2") && !parseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + parseMode + "\" does not exist.");

        if (!parseMode.equals("") && (captionEntities != null && !captionEntities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");
    }
}
