package org.example.api_request.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerPreCheckoutQuery implements ApiRequest<Boolean> {
    /**
     * Use this method to respond to pre-checkout queries.
     *
     * @param preCheckoutQueryId    Unique identifier for the query to be answered.
     * @param ok                    Specify True if everything is alright and the bot is ready to proceed with the order. Use False if there are any problems.
     * @param errorMessage          Error message in human-readable form that explains the reason for failure to proceed with the checkout (required if ok is False).
     */

    @NonNull
    @JsonProperty("pre_checkout_query_id")
    private String preCheckoutQueryId;

    @NonNull
    @JsonProperty("ok")
    private Boolean ok;

    @JsonProperty("error_message")
    private String errorMessage;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.ANSWER_PRE_CHECKOUT_QUERY.getPath();
    }

    @Override
    public void checkValidation() {
        if (!ok && (errorMessage == null || errorMessage.isEmpty())) {
            throw new IllegalArgumentException("errorMessage cannot be null or empty when ok is false.");
        }
    }
}
