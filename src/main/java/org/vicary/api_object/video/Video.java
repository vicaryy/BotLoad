package org.vicary.api_object.video;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.PhotoSize;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video implements ApiObject {
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("file_size")
    private Integer fileSize;
}
