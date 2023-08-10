package org.vicary.configuration;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GsonConfiguration {

    @Bean
    public Gson getGson() {
        return new Gson();
    }
}
