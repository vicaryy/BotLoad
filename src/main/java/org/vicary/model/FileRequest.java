package org.vicary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileRequest {
    private String URL;

    private String chatId;

    private String extension;

    private boolean premium;

    private int multiVideoNumber;

    private EditMessageText editMessageText;
}