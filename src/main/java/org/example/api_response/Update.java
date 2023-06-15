package org.example.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Update implements ApiResponseObject{
    @JsonProperty("update_id")
    private Integer updateId;
    @JsonProperty("message")
    private Message message;
//    @JsonProperty("edited_message")
//    private Message editedMessage;
//    @JsonProperty("channel_post")
//    private Message channelPost;
//    @JsonProperty("edited_channel_post")
//    private Message editedChannelPost;
//    @JsonProperty("inline_query")
//    private InlineQuery inlineQuery;
//    @JsonProperty("chosenInlineResult")
//    private ChosenInlineResult chosenInlineResult;
//    @JsonProperty("callbackQuery")
//    private CallbackQuery callbackQuery;
//    @JsonProperty("shipping_query")
//    private ShippingQuery shippingQuery;
//    @JsonProperty("pre_checkout_query")
//    private PreCheckoutQuery preCheckoutQuery;
//    @JsonProperty("poll")
//    private Poll poll;
//    @JsonProperty("poll_answer")
//    private PollAnswer pollAnswer;
//    @JsonProperty("my_chat_member")
//    private ChatMemberUpdated myChatMember;
//    @JsonProperty("chat_member")
//    private ChatMemberUpdated chatMember;
//    @JsonProperty("chat_join_request")
//    private ChatJoinRequest chatJoinRequest;
}
