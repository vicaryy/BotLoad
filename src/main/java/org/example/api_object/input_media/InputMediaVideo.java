package org.example.api_object.input_media;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.MessageEntity;
import org.example.api_request.InputFile;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class InputMediaVideo implements InputMedia {
    @JsonProperty("type")
    private final String type = "video";

    @NonNull
    @JsonProperty("media")
    private String media;

    @JsonProperty("thumbnail")
    private InputFile thumbnail;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("parse_mode")
    private String parseMode;

    @JsonProperty("caption_entities")
    private List<MessageEntity> captionEntities;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("supports_streaming")
    private Boolean supportsStreaming;

    @JsonProperty("has_spoiler")
    private Boolean hasSpoiler;
}
