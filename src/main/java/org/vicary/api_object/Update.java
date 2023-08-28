package org.vicary.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.chat.chat_member.ChatJoinRequest;
import org.vicary.api_object.chat.chat_member.ChatMemberUpdated;
import org.vicary.api_object.inline_query.ChosenInlineResult;
import org.vicary.api_object.inline_query.InlineQuery;
import org.vicary.api_object.message.Message;
import org.vicary.api_object.other.CallbackQuery;
import org.vicary.api_object.payments.PreCheckoutQuery;
import org.vicary.api_object.payments.ShippingQuery;
import org.vicary.api_object.poll.Poll;
import org.vicary.api_object.poll.PollAnswer;

@Getter
@ToString
@EqualsAndHashCode
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Update implements ApiObject {
    /**
     * This object represents an incoming update.
     *
     * @param updateId The update's unique identifier. Update identifiers start from a certain positive number and increase sequentially. This ID becomes especially handy if you're using webhooks, since it allows you to ignore repeated updates or to restore the correct update sequence, should they get out of order. If there are no new updates for at least a week, then identifier of the next update will be chosen randomly instead of sequentially.
     * @param message New incoming message of any kind - text, photo, sticker, etc.
     * @param editedMessage New version of a message that is known to the bot and was edited.
     * @param channelPost New incoming channel post of any kind - text, photo, sticker, etc.
     * @param editedChannelPost New version of a channel post that is known to the bot and was edited.
     * @param inlineQuery New incoming inline query.
     * @param chosenInlineResult The result of an inline query that was chosen by a user and sent to their chat partner. Please see our documentation on the feedback collecting for details on how to enable these updates for your bot.
     * @param callbackQuery New incoming callback query.
     * @param shippingQuery New incoming shipping query. Only for invoices with flexible price.
     * @param preCheckoutQuery New incoming pre-checkout query. Contains full information about checkout.
     * @param poll New poll state. Bots receive only updates about stopped polls and polls, which are sent by the bot.
     * @param pollAnswer A user changed their answer in a non-anonymous poll. Bots receive new votes only in polls that were sent by the bot itself.
     * @param myChatMember The bot's chat member status was updated in a chat. For private chats, this update is received only when the bot is blocked or unblocked by the user.
     * @param chatMember A chat member's status was updated in a chat. The bot must be an administrator in the chat and must explicitly specify “chat_member” in the list of allowed_updates to receive these updates.
     * @param chatJoinRequest A request to join the chat has been sent. The bot must have the can_invite_users administrator right in the chat to receive these updates.
     */
    @JsonProperty("update_id")
    private Integer updateId;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("edited_message")
    private Message editedMessage;

    @JsonProperty("channel_post")
    private Message channelPost;

    @JsonProperty("edited_channel_post")
    private Message editedChannelPost;

    @JsonProperty("inline_query")
    private InlineQuery inlineQuery;

    @JsonProperty("chosen_inline_result")
    private ChosenInlineResult chosenInlineResult;

    @JsonProperty("callback_query")
    private CallbackQuery callbackQuery;

    @JsonProperty("shipping_query")
    private ShippingQuery shippingQuery;

    @JsonProperty("pre_checkout_query")
    private PreCheckoutQuery preCheckoutQuery;

    @JsonProperty("poll")
    private Poll poll;

    @JsonProperty("poll_answer")
    private PollAnswer pollAnswer;

    @JsonProperty("my_chat_member")
    private ChatMemberUpdated myChatMember;

    @JsonProperty("chat_member")
    private ChatMemberUpdated chatMember;

    @JsonProperty("chat_join_request")
    private ChatJoinRequest chatJoinRequest;


    public String getChatId() {
        return message.getChat().getId().toString();
    }
}
