package org.example.api_object.telegram_passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class PassportData implements ApiObject {
    /**
     * Describes Telegram Passport data shared with the bot by the user.
     *
     * @param data        List with information about documents and other Telegram Passport elements that was shared with the bot
     * @param credentials Encrypted credentials required to decrypt the data
     */
    @JsonProperty("data")
    private List<EncryptedPassportElement> data;

    @JsonProperty("credentials")
    private EncryptedCredentials credentials;

    private PassportData() {
    }
}


