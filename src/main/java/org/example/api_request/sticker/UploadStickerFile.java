package org.example.api_request.sticker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.example.api_object.File;
import org.example.api_request.ApiRequest;
import org.example.api_request.InputFile;
import org.example.end_point.EndPoint;

@Data
@RequiredArgsConstructor
@Builder
public class UploadStickerFile implements ApiRequest<File> {
    /**
     * Use this method to upload a file with a sticker for later use in the createNewStickerSet and addStickerToSet methods
     * (the file can be used multiple times).
     *
     * @param userId       User identifier of sticker file owner.
     * @param sticker      A file with the sticker in .WEBP, .PNG, .TGS, or .WEBM format.
     * See https://core.telegram.org/stickers for technical requirements.
     * More information on Sending Files.
     * @param stickerFormat Format of the sticker, must be one of “static”, “animated”, “video”.
     */

    @JsonIgnore
    private final String methodName = "sticker";

    @NonNull
    @JsonProperty("user_id")
    private Integer userId;

    @NonNull
    @JsonProperty("sticker")
    private InputFile sticker;

    @NonNull
    @JsonProperty("sticker_format")
    private String stickerFormat;

    public void setStickerFormatOnStatic() {
        this.stickerFormat = "static";
    }

    public void setStickerFormatOnAnimated() {
        this.stickerFormat = "animated";
    }

    public void setStickerFormatOnVideo() {
        this.stickerFormat = "video";
    }

    @Override
    public File getReturnObject() {
        return new File();
    }

    @Override
    public String getEndPoint() {
        return EndPoint.UPLOAD_STICKER_FILE.getPath();
    }

    @Override
    public void checkValidation() {
        if (!stickerFormat.equals("static") && !stickerFormat.equals("animated") && !stickerFormat.equals("video"))
            throw new IllegalArgumentException("stickerFormat must be one of “static”, “animated”, or “video”.");
    }
}
