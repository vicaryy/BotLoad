package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse implements ApiObject {
    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("result")
    private List<Update> result;

    private ApiResponse() {
    }
}
