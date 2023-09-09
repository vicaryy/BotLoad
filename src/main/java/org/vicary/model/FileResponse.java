package org.vicary.model;

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
public class FileResponse {
    private String URL;

    private String serviceId;

    private String extension;

    private boolean premium;

    private String title;

    private int duration;

    private long size;

    private String telegramFileId;

    private String chatId;

    private int multiVideoNumber;

    private ID3Tag id3Tag;

    private InputFile downloadedFile;

    private InputFile thumbnail;

    private EditMessageText editMessageText;
}
