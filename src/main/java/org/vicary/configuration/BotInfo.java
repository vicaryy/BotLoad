package org.vicary.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BotInfo {
    private static ApiBotConfiguration apiBotConfiguration;

    @Autowired
    public BotInfo(ApiBotConfiguration apiBotConfiguration) {
        BotInfo.apiBotConfiguration = apiBotConfiguration;
    }

    public static String getDownloadDestination() {
        return apiBotConfiguration.getDownloadDestination();
    }

    public static String getURL() {
        return apiBotConfiguration.getApiUrl() + apiBotConfiguration.getBotToken();
    }

    public static String getBotUsername() {
        return apiBotConfiguration.getBotUsername();
    }

    public static String getBotToken() {
        return apiBotConfiguration.getBotToken();
    }
}
