package org.example.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;
import org.example.api_object.User;

@Getter
@ToString
@EqualsAndHashCode
public class PreCheckoutQuery implements ApiObject {
    /**
     * This object contains information about an incoming pre-checkout query.
     *
     * @param id                 Unique query identifier
     * @param from               User who sent the query
     * @param currency           Three-letter ISO 4217 currency code
     * @param totalAmount        Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145.
     * @param invoicePayload     Bot specified invoice payload
     * @param shippingOptionId   Identifier of the shipping option chosen by the user
     * @param orderInfo          Order information provided by the user
     */
    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private User from;

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

    private PreCheckoutQuery() {
    }
}

