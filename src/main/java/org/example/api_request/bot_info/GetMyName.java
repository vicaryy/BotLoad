package org.example.api_request.bot_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_object.bot.BotName;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyName implements ApiRequest<BotName> {
    /**
     * Use this method to get the current bot name for the given user language.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string.
     */
    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public BotName getReturnObject() {
        return new BotName();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_MY_NAME.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
