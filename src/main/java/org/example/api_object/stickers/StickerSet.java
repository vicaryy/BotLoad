package org.example.api_object.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.PhotoSize;

import java.util.List;

@Getter
public class StickerSet implements ApiObject {
    /**
     * This object represents a sticker set.
     */
    @JsonProperty("name")
    private String name;

    @JsonProperty("title")
    private String title;

    @JsonProperty("sticker_type")
    private String stickerType;

    @JsonProperty("is_animated")
    private Boolean isAnimated;

    @JsonProperty("is_video")
    private Boolean isVideo;

    @JsonProperty("stickers")
    private List<Sticker> stickers;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;

    private StickerSet() {
    }
}
