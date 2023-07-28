package org.example.service.thread;

import org.example.api_object.UpdateResponse;
import org.example.api_object.Update;
import org.example.configuration.BotInfo;
import org.example.end_point.EndPoint;
import org.example.service.UpdatePollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UpdatePollingThread implements Runnable {
    private final UpdatePollingService updatePollingService;
    private final WebClient client;
    private final Thread thread;
    private final ExecutorService executorService;
    private List<Update> updates;

    @Autowired
    public UpdatePollingThread(UpdatePollingService updatePollingService,
                               WebClient client) {
        this.updatePollingService = updatePollingService;
        this.client = client;

        this.thread = new Thread(this);
        thread.start();
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        while (true) {
            try {
                updates = getUpdates();
            } catch (Exception e) {
                System.out.println("Connection lost.");
            }
            if (updates != null) {
                for (Update update : updates)
                    executorService.execute(() -> updatePollingService.updateReceiver(update));
            }

            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                System.out.println("Something goes wrong.");
            }

        }
    }

    public List<Update> getUpdates() throws Exception {
        String pollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES.getPath();

        UpdateResponse response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();

        if (response.getResult().isEmpty())
            return null;

        int offset = ((Update) response.getResult().get(response.getResult().size() - 1)).getUpdateId() + 1;

        String deletePollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES_OFFSET.getPath() + offset;

        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
        return response.getResult();
    }
}
