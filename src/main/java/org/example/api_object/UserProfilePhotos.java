package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class UserProfilePhotos implements ApiObject {
    @JsonProperty("total_count")
    private Integer totalCount;

    @JsonProperty("photos")
    private List<List<PhotoSize>> photos;

    private UserProfilePhotos() {
    }
}
