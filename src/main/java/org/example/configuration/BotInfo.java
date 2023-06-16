package org.example.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotInfo {
    private static ApiBotConfiguration apiBotConfiguration;

    @Autowired
    public BotInfo(ApiBotConfiguration apiBotConfiguration) {
        BotInfo.apiBotConfiguration = apiBotConfiguration;
    }

    public static String GET_URL() {
        return apiBotConfiguration.getApiUrl() + "bot" + apiBotConfiguration.getBotToken();
    }

    public static String GET_BOT_USERNAME() {
        return apiBotConfiguration.getBotUsername();
    }

    public static String GET_BOT_TOKEN() {
        return apiBotConfiguration.getBotToken();
    }
}
