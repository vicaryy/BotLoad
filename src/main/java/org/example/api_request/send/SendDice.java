package org.example.api_request.send;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendDice implements ApiRequest<Message> {
    /**
     * Use this method to send an animated emoji that will display a random value. On success, the sent Message is returned.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param emoji                 Optional. Emoji on which the dice throw animation is based. Currently, must be one of “🎲”, “🎯”, “🏀”, “⚽”, “🎳”, or “🎰”. Defaults to “🎲”.
     * @param disableNotification   Optional. Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent        Optional. Protects the contents of the sent message from forwarding.
     * @param replyToMessageId      Optional. If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply Optional. Pass True if the message should be sent even if the specified replied-to message is not found.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

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

    public void setEmojiOnDice() {
        this.emoji = "\uD83C\uDFB2";
    }

    public void setEmojiOnDartBoard() {
        this.emoji = "\uD83C\uDFAF";
    }

    public void setEmojiOnBasketball() {
        this.emoji = "\uD83C\uDFC0";
    }

    public void setEmojiOnFootball() {
        this.emoji = "⚽";
    }

    public void setEmojiOnBowling() {
        this.emoji = "\uD83C\uDFB3";
    }

    public void setEmojiOnRoulette() {
        this.emoji = "\uD83C\uDFB0";
    }

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_DICE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("Chat id cannot be empty.");

        if (emoji == null) setEmojiOnDice();

        if (!emoji.equals("\uD83C\uDFB2")
                && !emoji.equals("\uD83C\uDFAF")
                && !emoji.equals("\uD83C\uDFC0")
                && !emoji.equals("⚽")
                && !emoji.equals("\uD83C\uDFB3")
                && !emoji.equals("\uD83C\uDFB0"))
            throw new IllegalArgumentException("Emoji: \"" + emoji + "\" does not exist.");
    }
}
