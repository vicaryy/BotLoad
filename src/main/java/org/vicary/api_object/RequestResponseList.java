package org.vicary.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class RequestResponseList<T> implements ApiObject {
    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("result")
    private List<T> result;
}