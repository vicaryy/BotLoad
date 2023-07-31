package org.vicary.api_object.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.vicary.api_object.ApiObject;
import org.vicary.api_object.PhotoSize;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
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
}
