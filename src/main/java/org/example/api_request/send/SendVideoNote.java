package org.example.api_request.send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_request.ApiRequest;
import org.example.api_request.InputFile;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendVideoNote implements ApiRequest<Message> {
    /**
     * Use this method to send video messages. On success, the sent Message is returned.
     *
     * @param chatId                  Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId         Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param videoNote               Video note to send. Pass a file_id as String to send a video note that exists on the Telegram servers (recommended)
     *                                or upload a new video using multipart/form-data. More information on Sending Files. Sending video notes by a URL is currently unsupported.
     * @param duration                Optional. Duration of sent video in seconds.
     * @param length                  Optional. Video width and height, i.e., diameter of the video message.
     * @param thumbnail               Optional. Thumbnail of the file sent; can be ignored if thumbnail generation for the file is supported server-side.
     *                                The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail's width and height should not exceed 320.
     *                                Ignored if the file is not uploaded using multipart/form-data. Thumbnails can't be reused and can be only uploaded as a new file,
     *                                so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>.
     *                                More information on Sending Files.
     * @param disableNotification     Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent          Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId        Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup             Optional. Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard,
     *                                instructions to remove reply keyboard, or to force a reply from the user.
     */
    @JsonIgnore
    private final String methodName = "video_note";

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("video_note")
    private InputFile videoNote;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("thumbnail")
    private InputFile thumbnail;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")
//    private Object replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_VIDEO_NOTE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
