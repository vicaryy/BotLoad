package org.example.thread;

import org.example.api_response.ApiResponse;
import org.example.api_response.Update;
import org.example.configuration.ApiBotConfiguration;
import org.example.end_point.EndPoint;
import org.example.service.UpdatePollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UpdatePollingThread implements Runnable {
    private final UpdatePollingService updatePollingService;
    private final ApiBotConfiguration apiBotConfiguration;
    private final RestTemplate restTemplate;
    private final Thread thread;

    @Autowired
    public UpdatePollingThread(UpdatePollingService updatePollingService,
                               ApiBotConfiguration apiBotConfiguration,
                               RestTemplate restTemplate) {
        this.updatePollingService = updatePollingService;
        this.apiBotConfiguration = apiBotConfiguration;
        this.restTemplate = restTemplate;

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
        String pollUrl = apiBotConfiguration.getUrl() + EndPoint.GET_UPDATES.getPath() + "-1";
        ResponseEntity<ApiResponse> entity = restTemplate.getForEntity(pollUrl, ApiResponse.class);
        if (entity.getBody().getResult().isEmpty()) {
            return null;
        }
        Update update = entity.getBody().getResult().get(0);
        String deletePollUrl = apiBotConfiguration.getUrl() + EndPoint.GET_UPDATES.getPath() + (update.getUpdateId() + 1);
        restTemplate.getForEntity(deletePollUrl, ApiResponse.class);
        return update;
    }
}
