package org.vicary.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vicary.command.YtDlpCommand;

@Component
public class BotInfo {
    private static ApiBotConfiguration apiBotConfiguration;
    private static YtDlpCommand command;

    @Autowired
    public BotInfo(ApiBotConfiguration apiBotConfiguration,
                   YtDlpCommand command) {
        BotInfo.apiBotConfiguration = apiBotConfiguration;
        BotInfo.command = command;
    }

    public static String getDownloadDestination() {
        return command.getDownloadDestination();
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
