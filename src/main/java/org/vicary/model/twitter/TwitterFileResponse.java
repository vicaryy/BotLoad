package org.vicary.model.twitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwitterFileResponse {
    private String url;

    private String twitterId;

    private String extension;

    private Boolean premium;

    private String title;

    private Integer duration;

    private Long size;

    private InputFile downloadedFile;

    private EditMessageText editMessageText;
}
