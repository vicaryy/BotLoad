package org.example.api_object.input_media;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.api_object.message.MessageEntity;
import org.example.api_request.InputFile;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class InputMediaAudio implements InputMedia {
    @JsonProperty("type")
    private final String type = "audio";

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

    @JsonProperty("duration")
    private Integer duration;

    @JsonProperty("performer")
    private String performer;

    @JsonProperty("title")
    private String title;
}
