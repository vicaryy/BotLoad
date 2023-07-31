package org.vicary.api_request.bot_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.bot.BotShortDescription;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyShortDescription implements ApiRequest<BotShortDescription> {
    /**
     * Use this method to get the current bot short description for the given user language.
     *
     * @param languageCode A two-letter ISO 639-1 language code or an empty string.
     */
    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public BotShortDescription getReturnObject() {
        return new BotShortDescription();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.GET_MY_SHORT_DESCRIPTION.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
