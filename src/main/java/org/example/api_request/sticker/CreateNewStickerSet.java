package org.example.api_request.sticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.stickers.InputSticker;
import org.example.api_request.ApiRequest;
import org.example.end_point.EndPoint;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class CreateNewStickerSet implements ApiRequest<Boolean> {
    /**
     * Use this method to create a new sticker set owned by a user. The bot will be able to edit the sticker set thus created.
     *
     * @param userId         User identifier of created sticker set owner.
     * @param name           Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals).
     * Can contain only English letters, digits, and underscores.
     * Must begin with a letter, can't contain consecutive underscores, and must end in "_by_<bot_username>".
     * <bot_username> is case insensitive. 1-64 characters.
     * @param title          Sticker set title, 1-64 characters.
     * @param stickers       A JSON-serialized list of 1-50 initial stickers to be added to the sticker set.
     * @param stickerFormat  Format of stickers in the set, must be one of “static”, “animated”, “video”.
     * @param stickerType    Type of stickers in the set, pass “regular”, “mask”, or “custom_emoji”. By default, a regular sticker set is created.
     * @param needsRepainting Pass True if stickers in the sticker set must be repainted to the color of text when used in messages,
     * the accent color if used as emoji status, white on chat photos, or another appropriate color based on context;
     * for custom emoji sticker sets only.
     */

    //TODO - THIS CLASS WILL NOT WORK PROPERLY.


    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("name")
    private String name;

    @NonNull
    @JsonProperty("title")
    private String title;

    @NonNull
    @JsonProperty("stickers")
    private List<InputSticker> stickers;

    @NonNull
    @JsonProperty("sticker_format")
    private String stickerFormat;

    @JsonProperty("sticker_type")
    private String stickerType;

    @JsonProperty("needs_repainting")
    private Boolean needsRepainting;

    @Override
    public Boolean getReturnObject() {
        return true;
    }

    @Override
    public String getEndPoint() {
        return EndPoint.CREATE_NEW_STICKER_SET.getPath();
    }

    @Override
    public void checkValidation() {
        if (!stickerFormat.equals("static") && !stickerFormat.equals("animated") && !stickerFormat.equals("video")) {
            throw new IllegalArgumentException("stickerFormat must be one of “static”, “animated”, or “video”.");
        }

        if (stickerType == null) stickerType = "regular";

        if (!stickerType.isEmpty() && !stickerType.equals("regular")
                && !stickerType.equals("mask") && !stickerType.equals("custom_emoji")) {
            throw new IllegalArgumentException("stickerType must be one of “regular”, “mask”, or “custom_emoji”.");
        }
    }
}
