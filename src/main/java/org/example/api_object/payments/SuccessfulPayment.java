package org.example.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class SuccessfulPayment implements ApiObject {
    /**
     * This object contains basic information about a successful payment.
     *
     * @param currency                  Three-letter ISO 4217 currency code
     * @param totalAmount               Total price in the smallest units of the currency (integer, not float/double).
     *                                  For example, for a price of US$ 1.45 pass amount = 145.
     * @param invoicePayload            Bot specified invoice payload
     * @param shippingOptionId          Optional. Identifier of the shipping option chosen by the user
     * @param orderInfo                 Optional. Order information provided by the user
     * @param telegramPaymentChargeId   Telegram payment identifier
     * @param providerPaymentChargeId   Provider payment identifier
     */
    @JsonProperty("currency")
    private String currency;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    @JsonProperty("invoice_payload")
    private String invoicePayload;

    @JsonProperty("shipping_option_id")
    private String shippingOptionId;

    @JsonProperty("order_info")
    private OrderInfo orderInfo;

    @JsonProperty("telegram_payment_charge_id")
    private String telegramPaymentChargeId;

    @JsonProperty("provider_payment_charge_id")
    private String providerPaymentChargeId;

    private SuccessfulPayment() {
    }
}
