package org.example.api_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_object.chat.ChatAdministratorRights;
import org.example.end_point.EndPoint;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetMyDefaultAdministratorRights implements ApiRequest<Boolean> {
    /**
     * Use this method to change the default administrator rights requested by the bot
     * when it's added as an administrator to groups or channels.
     *
     * @param rights       A JSON-serialized object describing new default administrator rights.
     * If not specified, the default administrator rights will be cleared.
     * @param forChannels  Pass True to change the default administrator rights of the bot in channels.
     * Otherwise, the default administrator rights of the bot for groups and supergroups will be changed.
     */
    @JsonProperty("rights")
    private ChatAdministratorRights rights;

    @JsonProperty("for_channels")
    private Boolean forChannels;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.SET_MY_DEFAULT_ADMINISTRATOR_RIGHTS.getPath();
    }

    @Override
    public void checkValidation() {
    }
}
