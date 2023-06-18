package org.example.api_object.stickers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.api_object.ApiObject;

@Getter
public class MaskPosition implements ApiObject {
    /**
     * This object describes the position on faces where a mask should be placed by default.
     */
    @JsonProperty("point")
    private String point;

    @JsonProperty("x_shift")
    private Float xShift;

    @JsonProperty("y_shift")
    private Float yShift;

    @JsonProperty("scale")
    private Float scale;

    private MaskPosition() {
    }
}
