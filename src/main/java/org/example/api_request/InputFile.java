package org.example.api_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputFile {
    /**
     * This object represents the contents of a file to be uploaded. Must be posted using multipart/form-data in the usual way that files are uploaded via the browser.
     *
     * @param fileId - type fileId if file exist on Telegram server (recommended).
     * @param file - insert new File if file does not exist on Telegram server but if file already exist you can insert too.
     * @param isExistOnTelegram - true if exists, false if not.
     * @param isThumbnail - true if is thumbnail, false if not.
     */
    private String fileId;
    private File file;
    private boolean isExistOnTelegram;
    private boolean isThumbnail;

    public void isValid() {
        if(fileId == null || file == null)
            throw new IllegalArgumentException("Both fileId and file cannot be null.");
        if(isThumbnail && file == null)
            throw new IllegalArgumentException("Thumbnail must be new file.");
    }
}
