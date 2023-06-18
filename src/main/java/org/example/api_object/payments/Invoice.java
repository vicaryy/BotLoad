package org.example.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class Invoice implements ApiObject {
    /**
     * This object contains basic information about an invoice.
     *
     * @param title           Product name
     * @param description     Product description
     * @param startParameter  Unique bot deep-linking parameter that can be used to generate this invoice
     * @param currency        Three-letter ISO 4217 currency code
     * @param totalAmount     Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145.
     */
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("start_parameter")
    private String startParameter;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    private Invoice() {
    }
}
