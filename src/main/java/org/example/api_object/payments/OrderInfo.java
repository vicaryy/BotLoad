package org.example.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class OrderInfo implements ApiObject {
    /**
     * This object represents information about an order.
     *
     * @param name             User name
     * @param phoneNumber      User's phone number
     * @param email            User email
     * @param shippingAddress  User shipping address
     */
    @JsonProperty("name")
    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("shipping_address")
    private ShippingAddress shippingAddress;

    private OrderInfo() {
    }
}
