package org.example.api_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.api_object.ApiObject;
import org.example.controller.PostController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.NoSuchElementException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputFile implements ApiObject {
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

    public void checkValidation(String methodName) {
        if (fileId == null && file == null)
            throw new IllegalArgumentException("Both fileId and file cannot be null.");
        if (isThumbnail && file == null)
            throw new IllegalArgumentException("Thumbnail has to be a new file.");
        if (!file.exists())
            throw new NoSuchElementException("File does not exist. \nFile path: " + file.getPath());

        String fileName = file.getName().toLowerCase();
        if (methodName.equals("photo"))
            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png"))
                throw new IllegalArgumentException("Wrong file extension for photo. \nFile name: " + fileName);

        if (methodName.equals("audio"))
            if (!fileName.endsWith(".mp3") && !fileName.endsWith(".m4a"))
                throw new IllegalArgumentException("Wrong file extension for audio. \nFile name: " + fileName);

        if (methodName.equals("thumbnail")) {

            if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg"))
                throw new IllegalArgumentException("Wrong file extension for thumbnail. \nFile name: " + fileName);

            long fileSize = file.length() / 1024;
            if (fileSize > 200)
                throw new IllegalArgumentException("Size of thumbnail file cannot be more than 200kB." +
                        " \nFile size: " + fileSize + "kB");

            try {
                BufferedImage thumbnail = ImageIO.read(file);
                if (thumbnail.getWidth() > 320 || thumbnail.getHeight() > 320)
                    throw new IllegalArgumentException("A thumbnail's width and height should not exceed 320." +
                            " \nImage width: " + thumbnail.getWidth() +
                            " \nImage height: " + thumbnail.getHeight());
            } catch (Exception e) {
            }
        }
    }
}
