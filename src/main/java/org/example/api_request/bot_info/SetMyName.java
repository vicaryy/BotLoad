package org.example.api_request.bot_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetMyName implements ApiRequest<Boolean> {
    /**
     * Use this method to change the bot's name.
     *
     * @param name         New bot name; 0-64 characters. Pass an empty string to remove the dedicated name for the given language.
     * @param languageCode A two-letter ISO 639-1 language code. If empty, the name will be shown to all users for whose language there is no dedicated name.
     */
    @JsonProperty("name")
    private String name;

    @JsonProperty("language_code")
    private String languageCode;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_MY_NAME.getPath();
    }

    @Override
    public void checkValidation() {
        if(languageCode == null) languageCode = "";
    }
}
