package org.vicary.api_object.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.ApiObject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingOption implements ApiObject {
    /**
     * This object represents one shipping option.
     *
     * @param id      Shipping option identifier
     * @param title   Option title
     * @param prices  List of price portions
     */
    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("prices")
    private List<LabeledPrice> prices;
}
