package org.vicary.api_request.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.payments.LabeledPrice;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInvoiceLink implements ApiRequest<String> {
    /**
     * Use this method to create a link for an invoice.
     *
     * @param title                 Product name, 1-32 characters.
     * @param description           Product description, 1-255 characters.
     * @param payload               Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
     * @param providerToken         Payment provider token, obtained via BotFather.
     * @param currency              Three-letter ISO 4217 currency code, see more on currencies.
     * @param prices                Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.).
     * @param maxTipAmount          The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). Defaults to 0.
     * @param suggestedTipAmounts   A JSON-serialized array of suggested amounts of tips in the smallest units of the currency (integer, not float/double).
     * @param providerData          JSON-serialized data about the invoice, which will be shared with the payment provider.
     * @param photoUrl              URL of the product photo for the invoice.
     * @param photoSize             Photo size in bytes.
     * @param photoWidth            Photo width.
     * @param photoHeight           Photo height.
     * @param needName              Pass True if you require the user's full name to complete the order.
     * @param needPhoneNumber       Pass True if you require the user's phone number to complete the order.
     * @param needEmail             Pass True if you require the user's email address to complete the order.
     * @param needShippingAddress   Pass True if you require the user's shipping address to complete the order.
     * @param sendPhoneNumberToProvider   Pass True if the user's phone number should be sent to the provider.
     * @param sendEmailToProvider   Pass True if the user's email address should be sent to the provider.
     * @param isFlexible            Pass True if the final price depends on the shipping method.
     */

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

    @Override
    public String getReturnObject() {
        return "";
    }

    @Override
    public String getEndPoint() {
        return EndPoint.CREATE_INVOICE_LINK.getPath();
    }

    @Override
    public void checkValidation() {
        if (title.length() < 1 || title.length() > 32)
            throw new IllegalArgumentException("title has to be 1-32 characters.");
    }
}
