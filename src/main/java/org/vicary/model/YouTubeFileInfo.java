package org.vicary.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YouTubeFileInfo {
    @SerializedName("id")
    private String youtubeId;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("artist")
    private String artist;

    @SerializedName("track")
    private String track;

    @SerializedName("album")
    private String album;

    @SerializedName("release_year")
    private String releaseYear;
}
