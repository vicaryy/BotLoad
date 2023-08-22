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

    private String id;

    private String extension;

    private boolean premium;

    private String title;

    private int duration;

    private long size;

    private String artist;

    private String track;

    private String album;

    private String releaseYear;

    private int multiVideoNumber;

    private InputFile downloadedFile;

    private InputFile thumbnail;

    private EditMessageText editMessageText;
}
