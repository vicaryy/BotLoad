package org.vicary.api_object.inline_query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.other.WebAppInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineQueryResultsButton implements ApiObject {
    /**
     * This object represents a button to be shown above inline query results. You must use exactly one of the optional fields.
     */
    @JsonProperty("text")
    public String text;

    @JsonProperty("web_app")
    public WebAppInfo webApp;

    @JsonProperty("start_parameter")
    public String startParameter;
}
