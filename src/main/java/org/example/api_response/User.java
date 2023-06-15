package org.example.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements ApiResponseObject {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("is_bot")
    private Boolean isBot;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("username")
    private String username;
    @JsonProperty("language_code")
    private String languageCode;
}
