package org.vicary.api_object.telegram_passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class EncryptedCredentials implements ApiObject {
    /**
     * Describes data required for decrypting and authenticating EncryptedPassportElement.
     *
     * @param data   Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes, and secrets required for EncryptedPassportElement decryption and authentication.
     * @param hash   Base64-encoded data hash for data authentication.
     * @param secret Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption.
     */
    @JsonProperty("data")
    private String data;

    @JsonProperty("hash")
    private String hash;

    @JsonProperty("secret")
    private String secret;

    private EncryptedCredentials() {
    }
}
