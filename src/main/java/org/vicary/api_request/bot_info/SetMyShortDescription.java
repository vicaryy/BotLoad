package org.vicary.api_request.bot_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.ApiRequest;
import org.vicary.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetMyShortDescription implements ApiRequest<Boolean> {
    /**
     * Use this method to set the bot's short description.
     *
     * @param shortDescription New short description for the bot; 0-120 characters.
     *                         Pass an empty string to remove the dedicated short description for the given language.
     * @param languageCode     A two-letter ISO 639-1 language code. If empty, the short description will be applied to
     *                         all users for whose language there is no dedicated short description.
     */
    @JsonProperty("short_description")
    private String shortDescription;

    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_MY_SHORT_DESCRIPTION.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
