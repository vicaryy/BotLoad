package org.vicary.service.thread;

import org.vicary.api_object.UpdateResponse;
import org.vicary.api_object.Update;
import org.vicary.configuration.BotInfo;
import org.vicary.end_point.EndPoint;
import org.vicary.service.UpdateReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UpdatePollingThread implements Runnable {
    private final UpdateReceiverService updateReceiverService;
    private final WebClient client;
    private final Thread thread;
    private final ExecutorService executorService;
    private List<Update> updates;

    @Autowired
    public UpdatePollingThread(UpdateReceiverService updateReceiverService,
                               WebClient client) {
        this.updateReceiverService = updateReceiverService;
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
            if (updates != null && updates.size() < 6)
                updates.stream()
                        .forEach(update
                                -> executorService.execute(()
                                -> updateReceiverService.updateReceiver(update)));

            try {
                Thread.sleep(1500);
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
