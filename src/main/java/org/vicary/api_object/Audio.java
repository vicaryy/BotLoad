package org.vicary.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
public class Audio implements ApiObject {
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("performer")
    private String performer;

    @JsonProperty("title")
    private String title;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("file_size")
    private Integer fileSize;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;
}
