package org.vicary.api_request.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.inline_query.SentWebAppMessage;
import org.vicary.api_object.inline_query.inline_query_result.InlineQueryResult;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class AnswerWebAppQuery implements ApiRequest<SentWebAppMessage> {
    /**
     * Use this method to set the result of an interaction with a Web App and send a corresponding message
     * on behalf of the user to the chat from which the query originated.
     *
     * @param webAppQueryId Unique identifier for the query to be answered.
     * @param result        A JSON-serialized object describing the message to be sent.
     */

    @NonNull
    @JsonProperty("web_app_query_id")
    private String webAppQueryId;

    @NonNull
    @JsonProperty("result")
    private InlineQueryResult result;

    @Override
    public SentWebAppMessage getReturnObject() {
        return new SentWebAppMessage();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.ANSWER_WEBAPP_QUERY.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
