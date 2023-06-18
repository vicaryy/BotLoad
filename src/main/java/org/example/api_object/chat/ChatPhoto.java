package org.example.api_object.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.api_object.ApiObject;

@Getter
@ToString
@EqualsAndHashCode
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
