package org.vicary.api_object.telegram_passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class EncryptedPassportElement implements ApiObject {
    /**
     * Describes documents or other Telegram Passport elements shared with the bot by the user.
     *
     * @param type          Element type. One of “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport”, “address”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration”, “temporary_registration”, “phone_number”, “email”.
     * @param data          Base64-encoded encrypted Telegram Passport element data provided by the user, available for “personal_details”, “passport”, “driver_license”, “identity_card”, “internal_passport” and “address” types. Can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param phone_number  User's verified phone number, available only for “phone_number” type.
     * @param email         User's verified email address, available only for “email” type.
     * @param files         List of encrypted files with documents provided by the user, available for “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param front_side    Encrypted file with the front side of the document, provided by the user. Available for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param reverse_side  Encrypted file with the reverse side of the document, provided by the user. Available for “driver_license” and “identity_card”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param selfie        Encrypted file with the selfie of the user holding a document, provided by the user; available for “passport”, “driver_license”, “identity_card” and “internal_passport”. The file can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param translation   List of encrypted files with translated versions of documents provided by the user. Available if requested for “passport”, “driver_license”, “identity_card”, “internal_passport”, “utility_bill”, “bank_statement”, “rental_agreement”, “passport_registration” and “temporary_registration” types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
     * @param hash          Base64-encoded element hash for using in PassportElementErrorUnspecified.
     */
    @JsonProperty("type")
    private String type;

    @JsonProperty("data")
    private String data;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("files")
    private List<PassportFile> files;

    @JsonProperty("front_side")
    private PassportFile frontSide;

    @JsonProperty("reverse_side")
    private PassportFile reverseSide;

    @JsonProperty("selfie")
    private PassportFile selfie;

    @JsonProperty("translation")
    private List<PassportFile> translation;

    @JsonProperty("hash")
    private String hash;

    private EncryptedPassportElement() {
    }
}
