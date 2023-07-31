package org.vicary.api_object.telegram_passport;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.vicary.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
public class PassportFile implements ApiObject {
    /**
     * This object represents a file uploaded to Telegram Passport. Currently all Telegram Passport files are in JPEG format when decrypted and don't exceed 10MB.
     *
     * @param file_id        Identifier for this file, which can be used to download or reuse the file
     * @param file_unique_id Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
     * @param file_size      File size in bytes
     * @param file_date      Unix time when the file was uploaded
     */
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("file_size")
    private Integer fileSize;

    @JsonProperty("file_date")
    private Integer fileDate;

    private PassportFile() {
    }
}
