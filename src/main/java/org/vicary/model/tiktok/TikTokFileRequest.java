package org.vicary.model.tiktok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vicary.api_request.edit_message.EditMessageText;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TikTokFileRequest {

    private String URL;

    private String chatId;

    private final String extension = "mp4";

    private boolean premium;

    private EditMessageText editMessageText;

    public static TikTokFileRequestBuilder builder() {
        return new TikTokFileRequestBuilder();
    }

    public static class TikTokFileRequestBuilder {
        TikTokFileRequestBuilder() {
        }

        private String URL;

        private String chatId;

        private boolean premium;

        private EditMessageText editMessageText;

        public TikTokFileRequestBuilder URL(String url) {
            this.URL = url;
            return this;
        }

        public TikTokFileRequestBuilder chatId(String chatId) {
            this.chatId = chatId;
            return this;
        }

        public TikTokFileRequestBuilder premium(boolean premium) {
            this.premium = premium;
            return this;
        }

        public TikTokFileRequestBuilder editMessageText(EditMessageText editMessageText) {
            this.editMessageText = editMessageText;
            return this;
        }

        public TikTokFileRequest build() {
            return new TikTokFileRequest(
                    this.URL,
                    this.chatId,
                    this.premium,
                    this.editMessageText);
        }
    }
}
