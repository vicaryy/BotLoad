package org.vicary.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.ApiObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabeledPrice implements ApiObject {
    /**
     * This object represents a portion of the price for goods or services.
     *
     * @param label   Portion label
     * @param amount  Price of the product in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145.
     */
    @JsonProperty("label")
    private String label;

    @JsonProperty("amount")
    private Integer amount;
}
