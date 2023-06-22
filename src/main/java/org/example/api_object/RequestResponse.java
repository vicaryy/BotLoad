package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponse<T> implements ApiObject {
    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("result")
    private T result;
}
