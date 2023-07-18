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
public class SendAnimation implements ApiRequest<Message> {
    /**
     * Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound).
     * On success, the sent Message is returned.
     * Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId                       Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId              Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param animation                    Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended),
     * pass an HTTP URL as a String for Telegram to get an animation from the Internet,
     * or upload a new animation using multipart/form-data. More information on Sending Files.
     * @param duration                     Optional. Duration of the sent animation in seconds.
     * @param width                        Optional. Animation width.
     * @param height                       Optional. Animation height.
     * @param thumbnail                    Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320.
     * Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file,
     * so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     * More information on Sending Files.
     * @param caption                      Optional. Animation caption (may also be used when resending animation by file_id),
     * 0-1024 characters after entities parsing.
     * @param parseMode                    Optional. Mode for parsing entities in the animation caption. See formatting options for more details.
     * @param captionEntities              Optional. A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode.
     * @param hasSpoiler                   Optional. Pass True if the animation needs to be covered with a spoiler animation.
     * @param disableNotification          Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent               Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId             Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply     Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup                  Optional. Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard, or to force a reply from the user.
     */

    @JsonIgnore
    private final String methodName = "animation";

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("animation")
    private InputFile animation;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("thumbnail")
    private InputFile thumbnail;

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

//    @JsonProperty("reply_markup")                     // Does not work properly
//    private Object replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_ANIMATION.getPath();
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
