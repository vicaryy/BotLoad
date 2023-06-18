package org.example.api_object.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;
import org.example.api_object.File;
import org.example.api_object.PhotoSize;

@Getter
public class Sticker implements ApiObject {
    /**
     * This object represents a sticker
     */
    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_unique_id")
    private String fileUniqueId;

    @JsonProperty("type")
    private String type;

    @JsonProperty("width")
    private Integer width;

    @JsonProperty("height")
    private Integer height;

    @JsonProperty("is_animated")
    private Boolean isAnimated;

    @JsonProperty("is_video")
    private Boolean isVideo;

    @JsonProperty("thumbnail")
    private PhotoSize thumbnail;

    @JsonProperty("emoji")
    private String emoji;

    @JsonProperty("set_name")
    private String setName;

    @JsonProperty("premium_animation")
    private File premiumAnimation;

    @JsonProperty("mask_position")
    private MaskPosition maskPosition;

    @JsonProperty("custom_emoji_id")
    private String customEmojiId;

    @JsonProperty("needs_repainting")
    private Boolean needsRepainting;

    @JsonProperty("file_size")
    private Integer fileSize;

    private Sticker() {
    }
}
