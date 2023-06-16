package org.example.thread;

import org.example.api_object.ApiResponse;
import org.example.api_object.Update;
import org.example.configuration.BotInfo;
import org.example.end_point.EndPoint;
import org.example.service.UpdatePollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import javax.xml.datatype.Duration;

@Component
public class UpdatePollingThread implements Runnable {
    private final UpdatePollingService updatePollingService;
    private final WebClient client;
    private final Thread thread;

    @Autowired
    public UpdatePollingThread(UpdatePollingService updatePollingService,
                               WebClient client) {
        this.updatePollingService = updatePollingService;
        this.client = client;

        this.thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            Update update = pollUpdate();
            if (update != null)
                updatePollingService.updateReceiver(update);

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Something goes wrong.");
            }

        }
    }

    public Update pollUpdate() {
        String pollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES.getPath() + "-1";

        ApiResponse response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();

        System.out.println("siema");

        if (response.getResult().isEmpty())
            return null;

        Update update = response.getResult().get(0);
        String deletePollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES.getPath() + (update.getUpdateId() + 1);

        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .block();

        System.out.println("robie");

        return update;
    }
}
