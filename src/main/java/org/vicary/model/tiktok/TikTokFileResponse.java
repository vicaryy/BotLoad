package org.vicary.model.tiktok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.InputFile;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TikTokFileResponse {
    private String URL;

    private String tiktokId;

    private String extension;

    private boolean premium;

    private String title;

    private int duration;

    private Long size;

    private InputFile downloadedFile;

    private EditMessageText editMessageText;

    public static TikTokFileResponseBuilder builder() {
        return new TikTokFileResponseBuilder();
    }

    public static class TikTokFileResponseBuilder {
        TikTokFileResponseBuilder() {
        }

        private String URL;

        private String tiktokId;

        private String extension;

        private boolean premium;

        private String title;

        private int duration;

        private Long size;

        private InputFile downloadedFile;

        private EditMessageText editMessageText;

        public TikTokFileResponseBuilder URL(String URL) {
            this.URL = URL;
            return this;
        }

        public TikTokFileResponseBuilder tiktokId(String tiktokId) {
            this.tiktokId = tiktokId;
            return this;
        }

        public TikTokFileResponseBuilder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public TikTokFileResponseBuilder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public TikTokFileResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TikTokFileResponseBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public TikTokFileResponseBuilder size(Long size) {
            this.size = size;
            return this;
        }

        public TikTokFileResponseBuilder downloadedFile(InputFile downloadedFile) {
            this.downloadedFile = downloadedFile;
            return this;
        }

        public TikTokFileResponseBuilder editMessageText(EditMessageText editMessageText) {
            this.editMessageText = editMessageText;
            return this;
        }

        public TikTokFileResponse build() {
            return new TikTokFileResponse(
                    this.URL,
                    this.tiktokId,
                    this.extension,
                    this.premium,
                    this.title,
                    this.duration,
                    this.size,
                    this.downloadedFile,
                    this.editMessageText);
        }
    }
}
