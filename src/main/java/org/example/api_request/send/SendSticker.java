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
public class SendSticker implements ApiRequest<Message> {
    /**
     * Use this method to send static .WEBP, animated .TGS, or video .WEBM stickers.
     *
     * @param chatId                 Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId        Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param sticker                Sticker to send. Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
     *                               pass an HTTP URL as a String for Telegram to get a .WEBP sticker from the Internet, or upload a new .WEBP or .TGS sticker using multipart/form-data.
     *                               Video stickers can only be sent by a file_id. Animated stickers can't be sent via an HTTP URL.
     * @param emoji                  Emoji associated with the sticker; only for just uploaded stickers.
     * @param disableNotification    Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent         Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId       If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup            Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
     */

    @JsonIgnore
    private final String methodName = "sticker";

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("sticker")
    private InputFile sticker;

    @JsonProperty("emoji")
    private String emoji;

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
        return EndPoint.SEND_STICKER.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");
    }
}
