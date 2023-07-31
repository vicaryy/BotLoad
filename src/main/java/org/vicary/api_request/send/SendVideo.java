package org.vicary.api_request.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.message.Message;
import org.vicary.api_object.message.MessageEntity;
import org.vicary.api_request.ApiRequest;
import org.vicary.api_request.InputFile;
import org.vicary.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendVideo implements ApiRequest<Message> {
    /**
     * Use this method to send video files, Telegram clients support MPEG4 videos (other formats may be sent as Document).
     * On success, the sent Message is returned. Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
     *
     * @param chatId                       Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId              Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param video                        Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended),
     * pass an HTTP URL as a String for Telegram to get a video from the Internet,
     * or upload a new video using multipart/form-data. More information on Sending Files.
     * @param duration                     Optional. Duration of the sent video in seconds.
     * @param width                        Optional. Video width.
     * @param height                       Optional. Video height.
     * @param thumbnail                    Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     * The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320.
     * Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file,
     * so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     * More information on Sending Files.
     * @param caption                      Optional. Video caption (may also be used when resending videos by file_id), 0-1024 characters after entities parsing.
     * @param parseMode                    Optional. Mode for parsing entities in the video caption. See formatting options for more details.
     * @param captionEntities              Optional. A JSON-serialized list of special entities that appear in the caption, which can be specified instead of parse_mode.
     * @param hasSpoiler                   Optional. Pass True if the video needs to be covered with a spoiler animation.
     * @param supportsStreaming            Optional. Pass True if the uploaded video is suitable for streaming.
     * @param disableNotification          Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent               Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId             Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply     Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup                  Optional. Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     * instructions to remove reply keyboard, or to force a reply from the user.
     */
    @JsonIgnore
    private final String methodName = "video";

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("video")
    private InputFile video;

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

    @JsonProperty("supports_streaming")
    private Boolean supportsStreaming;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")                 // Does not work properly.
//    private Object replyMarkup;

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
        return EndPoint.SEND_VIDEO.getPath();
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
