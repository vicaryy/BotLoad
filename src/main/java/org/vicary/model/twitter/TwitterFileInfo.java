package org.vicary.model.twitter;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TwitterFileInfo {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private Double duration;

    @SerializedName("uploader_url")
    private String uploaderUrl;

    @SerializedName("webpage_url")
    private String URL;

    private final String extension = "mp4";

    public Integer getDuration() {
        return duration.intValue();
    }
}
