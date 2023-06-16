package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class VideoNote implements ApiObject {
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("length")
    private Integer length;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;

    @JsonProperty("file_size")
    private Integer fileSize;

    private VideoNote() {
    }
}
