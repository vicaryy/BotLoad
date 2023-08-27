package org.vicary.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class FileInfo {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("duration")
    private double duration;

    @SerializedName("artist")
    private String artist;

    @SerializedName("track")
    private String track;

    @SerializedName("album")
    private String album;

    @SerializedName("release_year")
    private String releaseYear;

    @SerializedName("uploader_url")
    private String uploaderURL;

    @SerializedName("is_live")
    private boolean isLive;

    @SerializedName("webpage_url")
    private String URL;

    public int getDuration() {
        return (int) Math.round(duration);
    }
}
