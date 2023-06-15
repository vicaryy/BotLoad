package org.example.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Setter
@Configuration
@ConfigurationProperties("telegram-bot")
public class ApiBotConfiguration {
    private String apiUrl;
    private String botUsername;
    private String botToken;

    public String getUrl() {
        return apiUrl + "bot" + botToken + "/";
    }

    public String getBotUsername() {
        return botUsername;
    }
}
