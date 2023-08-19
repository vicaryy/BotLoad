package org.vicary.model.twitter;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwitterFileInfo {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private Double duration;

    @SerializedName("uploader_url")
    private String uploaderUrl;

    private final String extension = "mp4";

    public Integer getDuration() {
        return duration.intValue();
    }
}
