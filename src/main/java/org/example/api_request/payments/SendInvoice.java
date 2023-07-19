package org.example.api_request.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.message.Message;
import org.example.api_object.payments.LabeledPrice;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendInvoice implements ApiRequest<Message> {
    /**
     * Use this method to send invoices.
     *
     * @param chatId                Unique identifier for the target chat or username of the target channel (in the format @channelusername).
     * @param messageThreadId       Unique identifier for the target message thread (topic) of the forum; for forum supergroups only.
     * @param title                 Product name, 1-32 characters.
     * @param description           Product description, 1-255 characters.
     * @param payload               Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
     * @param providerToken         Payment provider token, obtained via @BotFather.
     * @param currency              Three-letter ISO 4217 currency code, see more on currencies.
     * @param prices                Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.).
     * @param maxTipAmount          The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). Defaults to 0.
     * @param suggestedTipAmounts   A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double).
     * @param startParameter        Unique deep-linking parameter.
     * @param providerData          JSON-serialized data about the invoice, which will be shared with the payment provider.
     * @param photoUrl              URL of the product photo for the invoice.
     * @param photoSize             Photo size in bytes.
     * @param photoWidth            Photo width.
     * @param photoHeight           Photo height.
     * @param needName              Pass True if you require the user's full name to complete the order.
     * @param needPhoneNumber       Pass True if you require the user's phone number to complete the order.
     * @param needEmail             Pass True if you require the user's email address to complete the order.
     * @param needShippingAddress   Pass True if you require the user's shipping address to complete the order.
     * @param sendPhoneNumberToProvider   Pass True if the user's phone number should be sent to provider.
     * @param sendEmailToProvider   Pass True if the user's email address should be sent to provider.
     * @param isFlexible            Pass True if the final price depends on the shipping method.
     * @param disableNotification   Sends the message silently. Users will receive a notification with no sound.
     * @param protectContent        Protects the contents of the sent message from forwarding and saving.
     * @param replyToMessageId      If the message is a reply, ID of the original message.
     * @param allowSendingWithoutReply   Pass True if the message should be sent even if the specified replied-to message is not found.
     * @param replyMarkup           A JSON-serialized object for an inline keyboard. If empty, one 'Pay total price' button will be shown. If not empty, the first button must be a Pay button.
     */

    @NonNull
    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("message_thread_id")
    private Integer messageThreadId;

    @NonNull
    @JsonProperty("title")
    private String title;

    @NonNull
    @JsonProperty("description")
    private String description;

    @NonNull
    @JsonProperty("payload")
    private String payload;

    @NonNull
    @JsonProperty("provider_token")
    private String providerToken;

    @NonNull
    @JsonProperty("currency")
    private String currency;

    @NonNull
    @JsonProperty("prices")
    private List<LabeledPrice> prices;

    @JsonProperty("max_tip_amount")
    private Integer maxTipAmount;

    @JsonProperty("suggested_tip_amounts")
    private List<Integer> suggestedTipAmounts;

    @JsonProperty("start_parameter")
    private String startParameter;

    @JsonProperty("provider_data")
    private String providerData;

    @JsonProperty("photo_url")
    private String photoUrl;

    @JsonProperty("photo_size")
    private Integer photoSize;

    @JsonProperty("photo_width")
    private Integer photoWidth;

    @JsonProperty("photo_height")
    private Integer photoHeight;

    @JsonProperty("need_name")
    private Boolean needName;

    @JsonProperty("need_phone_number")
    private Boolean needPhoneNumber;

    @JsonProperty("need_email")
    private Boolean needEmail;

    @JsonProperty("need_shipping_address")
    private Boolean needShippingAddress;

    @JsonProperty("send_phone_number_to_provider")
    private Boolean sendPhoneNumberToProvider;

    @JsonProperty("send_email_to_provider")
    private Boolean sendEmailToProvider;

    @JsonProperty("is_flexible")
    private Boolean isFlexible;

    @JsonProperty("disable_notification")
    private Boolean disableNotification;

    @JsonProperty("protect_content")
    private Boolean protectContent;

    @JsonProperty("reply_to_message_id")
    private Integer replyToMessageId;

    @JsonProperty("allow_sending_without_reply")
    private Boolean allowSendingWithoutReply;

//    @JsonProperty("reply_markup")
//    private InlineKeyboardMarkup replyMarkup;

    @Override
    public Message getReturnObject() {
        return new Message();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SEND_INVOICE.getPath();
    }

    @Override
    public void checkValidation() {
        if (chatId.isEmpty()) throw new IllegalArgumentException("chatId cannot be empty.");

        if (title.length() < 1 || title.length() > 32)
            throw new IllegalArgumentException("title has to be 1-32 characters.");
    }
}
