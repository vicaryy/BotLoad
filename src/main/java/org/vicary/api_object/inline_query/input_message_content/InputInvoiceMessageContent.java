package org.vicary.api_object.inline_query.input_message_content;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.payments.LabeledPrice;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class InputInvoiceMessageContent implements InputMessageContent {
    /**
     * Represents the content of an invoice message to be sent as the result of an inline query.
     *
     * @param title                     Product name, 1-32 characters
     * @param description               Product description, 1-255 characters
     * @param payload                   Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
     * @param providerToken             Payment provider token, obtained via @BotFather
     * @param currency                  Three-letter ISO 4217 currency code, see more on currencies
     * @param prices                    Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
     * @param maxTipAmount              Optional. The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass maxTipAmount = 145.
     * @param suggestedTipAmounts       Optional. A JSON-serialized array of suggested amounts of tip in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed maxTipAmount.
     * @param providerData              Optional. A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed description of the required fields should be provided by the payment provider.
     * @param photoUrl                  Optional. URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
     * @param photoSize                 Optional. Photo size in bytes
     * @param photoWidth                Optional. Photo width
     * @param photoHeight               Optional. Photo height
     * @param needName                  Optional. Pass True if you require the user's full name to complete the order
     * @param needPhoneNumber           Optional. Pass True if you require the user's phone number to complete the order
     * @param needEmail                 Optional. Pass True if you require the user's email address to complete the order
     * @param needShippingAddress       Optional. Pass True if you require the user's shipping address to complete the order
     * @param sendPhoneNumberToProvider Optional. Pass True if the user's phone number should be sent to provider
     * @param sendEmailToProvider       Optional. Pass True if the user's email address should be sent to provider
     * @param isFlexible                Optional. Pass True if the final price depends on the shipping method
     */
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("payload")
    private String payload;

    @JsonProperty("provider_token")
    private String providerToken;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("prices")
    private List<LabeledPrice> prices;

    @JsonProperty("max_tip_amount")
    private Integer maxTipAmount;

    @JsonProperty("suggested_tip_amounts")
    private Integer[] suggestedTipAmounts;

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

    private InputInvoiceMessageContent() {
    }
}
