package org.vicary.configuration;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties("telegram-bot")
public class ApiBotConfiguration {

    private final String apiUrl = "https://api.telegram.org/";

    private String botUsername;

    private String botToken;

    private String downloadDestination;
}
