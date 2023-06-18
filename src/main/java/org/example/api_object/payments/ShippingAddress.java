package org.example.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class ShippingAddress implements ApiObject {
    /**
     * This object represents a shipping address.
     *
     * @param countryCode  Two-letter ISO 3166-1 alpha-2 country code
     * @param state        State, if applicable
     * @param city         City
     * @param streetLine1  First line for the address
     * @param streetLine2  Second line for the address
     * @param postCode     Address post code
     */
    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("state")
    private String state;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street_line1")
    private String streetLine1;

    @JsonProperty("street_line2")
    private String streetLine2;

    @JsonProperty("post_code")
    private String postCode;

    private ShippingAddress() {
    }
}
