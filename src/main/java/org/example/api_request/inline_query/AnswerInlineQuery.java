package org.example.api_request.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.inline_query.InlineQueryResultsButton;
import org.example.api_object.inline_query.inline_query_result.InlineQueryResult;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerInlineQuery implements ApiRequest<Boolean> {
    /**
     * Use this method to send answers to an inline query.
     *
     * @param inlineQueryId Unique identifier for the answered query.
     * @param results       A JSON-serialized array of results for the inline query.
     * @param cacheTime     The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
     * @param isPersonal    Pass True if results may be cached on the server side only for the user that sent the query.
     *                      By default, results may be returned to any user who sends the same query.
     * @param nextOffset    Pass the offset that a client should send in the next query with the same text to receive more results.
     *                      Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
     * @param button        A JSON-serialized object describing a button to be shown above inline query results.
     */

    @NonNull
    @JsonProperty("inline_query_id")
    private String inlineQueryId;

    @NonNull
    @JsonProperty("results")
    private List<InlineQueryResult> results;

    @JsonProperty("cache_time")
    private Integer cacheTime;

    @JsonProperty("is_personal")
    private Boolean isPersonal;

    @JsonProperty("next_offset")
    private String nextOffset;

    @JsonProperty("button")
    private InlineQueryResultsButton button;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.ANSWER_INLINE_QUERY.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
