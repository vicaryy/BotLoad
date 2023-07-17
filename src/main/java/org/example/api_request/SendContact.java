package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendContact implements ApiRequest<Message> {
    /**
     * Use this method to send phone contacts. On success, the sent Message is returned.
     *
     * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId     Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param phoneNumber         Contact's phone number.
     * @param firstName           Contact's first name.
     * @param lastName            Optional. Contact's last name.
     * @param vcard               Optional. Additional data about the contact in the form of a vCard, 0-2048 bytes.
     * @param disableNotification Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent      Optional. Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId    Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NonNull
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("vcard")
    private String vcard;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_CONTACT.getPath();
    }

    @Override
    public void checkValidation() {

    }
}
