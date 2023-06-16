package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatPhoto implements ApiObject {
    @JsonProperty("small_file_id")
    private String smallFileId;

    @JsonProperty("small_file_unique_id")
    private String smallFileUniqueId;

    @JsonProperty("big_file_id")
    private String bigFileId;

    @JsonProperty("big_file_unique_id")
    private String bigFileUniqueId;

    private ChatPhoto() {
    }
}
