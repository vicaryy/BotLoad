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
public class FileInfoThumbnail {

    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String URL;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("resolution")
    private String resolution;
}
