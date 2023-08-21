package org.vicary.model.tiktok;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TikTokFileInfo {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private int duration;

    @SerializedName("uploader_url")
    private String uploaderURL;

    @SerializedName("webpage_url")
    private String URL;

    private final String extension = "mp4";
}
