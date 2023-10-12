package org.vicary.api_request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_object.ApiObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private boolean isThumbnail;

    public void checkValidation(String methodName) {
        if (fileId != null && file != null)
            throw new IllegalArgumentException("You cannot give fileId and file, one of them has to be null.");
        if (fileId == null && file == null)
            throw new IllegalArgumentException("Both fileId and file cannot be null.");
        if (methodName.equals("thumbnail") && (fileId != null))
            throw new IllegalArgumentException("Thumbnail has to be a new file.");


        if (fileId == null) {
            long fileSize = file.length() / (1024 * 1024);
            if (fileSize > 50)
                throw new IllegalArgumentException("File size cannot be more than 50MB." +
                                                   "\n Your file size: " + fileSize + "MB.");
            String fileName = file.getName().toLowerCase();

            if (methodName.equals("photo"))
                photoValidation(fileName);

            else if (methodName.equals("audio"))
                audioValidation(fileName);

            else if (methodName.equals("video"))
                videoValidation(fileName);

            else if (methodName.equals("animation"))
                animationValidation(fileName);

            else if (methodName.equals("voice"))
                voiceValidation(fileName);

            else if (methodName.equals("thumbnail"))
                thumbnailValidation(fileName);

            else if (methodName.equals("sticker"))
                stickerValidation(fileName);
        }
    }


    public void photoValidation(String fileName) {
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png"))
            throw new IllegalArgumentException("Wrong file extension for photo. \nFile name: " + fileName);
    }

    public void videoValidation(String fileName) {
        if (!fileName.endsWith(".mp4") && !fileName.endsWith(".avi"))
            throw new IllegalArgumentException("Wrong file extension for audio. \nFile name: " + fileName);
    }

    public void audioValidation(String fileName) {
        if (!fileName.endsWith(".mp3") && !fileName.endsWith(".m4a"))
            throw new IllegalArgumentException("Wrong file extension for video. \nFile name: " + fileName);
    }

    public void animationValidation(String fileName) {
        if (!fileName.endsWith(".gif") && !fileName.endsWith(".mp4"))
            throw new IllegalArgumentException("Wrong file extension for animation. \nFile name: " + fileName);
    }

    public void voiceValidation(String fileName) {
        if (!fileName.endsWith(".ogg"))
            throw new IllegalArgumentException("Wrong file extension for voice. \nFile name: " + fileName);
    }

    public void thumbnailValidation(String fileName) {
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
        } catch (Exception ignored) {
        }
    }

    public void stickerValidation(String fileName) {
        if (!fileName.endsWith(".webp") && !fileName.endsWith(".tgs") && !fileName.endsWith(".webm"))
            throw new IllegalArgumentException("Wrong file extension for sticker. \nFile name: " + fileName);
    }
}
