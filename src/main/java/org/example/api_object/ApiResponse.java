package org.example.api_object;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements ApiObject {
    @JsonProperty("ok")
    private boolean ok;
    @JsonProperty("result")
    private List<Update> result;
}
