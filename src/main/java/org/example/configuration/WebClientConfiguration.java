package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientConfiguration {
    @Bean
    public WebClient getWebClient() {
        return WebClient.create();
    }
}
