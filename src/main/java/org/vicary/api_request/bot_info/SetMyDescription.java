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
public class SetMyDescription implements ApiRequest<Boolean> {
    /**
     * Use this method to change the bot's description, which is shown in the chat with the bot if the chat is empty.
     *
     * @param description   New bot description; 0-512 characters. Pass an empty string to remove the dedicated description for the given language.
     * @param languageCode  A two-letter ISO 639-1 language code. If empty, the description will be applied to all users for whose language there is no dedicated description.
     */
    @JsonProperty("description")
    private String description;

    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_MY_DESCRIPTION.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
