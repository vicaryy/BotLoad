package org.example.api_request.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_object.message.MessageEntity;
import org.example.api_request.ApiRequest;
import org.example.api_request.InputFile;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendPhoto implements ApiRequest<Message> {
    /**
     * Use this method to send photos. On success, the sent Message is returned.
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param photo                  Photo to send. Pass a file_id as String to send a photo that exists on the Telegram servers (recommended),
     * pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data.
     * The photo must be at most 10 MB in size. The photo's width and height must not exceed 10000 in total. Width and height ratio must be at most 20.
     * More information on Sending Files »
     * @param caption                Photo caption (may also be used when resending photos by file_id), 0-1024 characters after entities parsing.
     * @param parseMode              Mode for parsing entities in the photo caption. See formatting options for more details.
     * @param captionEntities        A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode.
     * @param hasSpoiler             Pass True if the photo needs to be covered with a spoiler animation.
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId       If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup            Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard or to force a reply from the user.
     */

    @JsonIgnore
    private final String methodName = "photo";

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("photo")
    private InputFile photo;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("has_spoiler")
    private Boolean hasSpoiler;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")                 Does not work properly.
//    private InlineReplyMarkup replyMarkup;

    public void setParseModeOnMarkdownV2() {
        parseMode = "MarkdownV2";
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
        return EndPoint.SEND_PHOTO.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");

        if (parseMode == null)
            parseMode = "";

        if (!parseMode.equals("HTML") && !parseMode.equals("MarkdownV2") && !parseMode.equals(""))
            throw new IllegalArgumentException("ParseMode: \"" + parseMode + "\" does not exist.");

        if (!parseMode.equals("") && (captionEntities != null && !captionEntities.isEmpty()))
            throw new IllegalArgumentException("If entities are provided, the parse mode cannot be active.");
    }
}
