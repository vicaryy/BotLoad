package org.example.api_request.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.payments.ShippingOption;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerShippingQuery implements ApiRequest<Boolean> {
    /**
     * Use this method to reply to shipping queries.
     *
     * @param shippingQueryId   Unique identifier for the query to be answered.
     * @param ok                Pass True if delivery to the specified address is possible and False if there are any problems.
     * @param shippingOptions   A JSON-serialized array of available shipping options (required if ok is True).
     * @param errorMessage      Error message in human readable form that explains why it is impossible to complete the order (required if ok is False).
     */

    @NonNull
    @JsonProperty("shipping_query_id")
    private String shippingQueryId;

    @NonNull
    @JsonProperty("ok")
    private Boolean ok;

    @JsonProperty("shipping_options")
    private List<ShippingOption> shippingOptions;

    @JsonProperty("error_message")
    private String errorMessage;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.ANSWER_SHIPPING_QUERY.getPath();
    }

    @Override
    public void checkValidation() {
        if (ok && (shippingOptions == null || shippingOptions.isEmpty())) {
            throw new IllegalArgumentException("shippingOptions cannot be null or empty when ok is true.");
        }

        if (!ok && (errorMessage == null || errorMessage.isEmpty())) {
            throw new IllegalArgumentException("errorMessage cannot be null or empty when ok is false.");
        }
    }
}
