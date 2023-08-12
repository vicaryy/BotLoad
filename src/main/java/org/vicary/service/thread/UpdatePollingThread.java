package org.vicary.service.thread;

import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.vicary.api_object.UpdateResponse;
import org.vicary.api_object.Update;
import org.vicary.configuration.BotInfo;
import org.vicary.end_point.EndPoint;
import org.vicary.exception.BotSendException;
import org.vicary.service.ActiveRequestService;
import org.vicary.service.UpdateReceiverService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UpdatePollingThread implements Runnable {
    private final UpdateReceiverService updateReceiverService;
    private final ActiveRequestService activeRequestService;
    private final WebClient client;
    private final Thread thread;
    private final ExecutorService executorService;
    private List<Update> updates;

    private static final int BREAK_BEFORE_START = 1000; // milliseconds
    private static final int TRYING_TO_RECONNECT_DELAY = 4000; // milliseconds
    private static final int EXECUTING_THREADS_DELAY = 150; // milliseconds
    private static final int GET_UPDATES_DELAY = 1500; // milliseconds
    private static final int MAX_UPDATES_SIZE = 6;

    public UpdatePollingThread(UpdateReceiverService updateReceiverService,
                               ActiveRequestService activeRequestService,
                               WebClient client) {
        this.updateReceiverService = updateReceiverService;
        this.activeRequestService = activeRequestService;
        this.client = client;

        this.thread = new Thread(this);
        thread.start();
        executorService = Executors.newCachedThreadPool();
        activeRequestService.deleteAllActiveUsers();
    }

    @Override
    public void run() {
        sleep(BREAK_BEFORE_START);

        while (!Thread.currentThread().isInterrupted()) {
            try {
                updates = getUpdates();
            } catch (WebClientResponseException ex) {
                handleWebClientResponseException(ex);
                break;
            } catch (WebClientRequestException ex) {
                handleWebClientRequestException(ex);
            }

            executeUpdates();
            sleep(GET_UPDATES_DELAY);
        }
    }

    public void executeUpdates() {
        if (updates != null && updates.size() < MAX_UPDATES_SIZE) {
            for (Update update : updates) {
                executorService.execute(() -> updateReceiverService.updateReceiver(update));
                sleep(EXECUTING_THREADS_DELAY);
            }
        }
    }

    public StackTraceElement printLocation() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2)
            return stackTrace[2];
        return null;
    }

    public void handleWebClientResponseException(WebClientResponseException ex) {
        System.err.println("---------------------------");
        System.err.println("Error in Polling Thread:");
        System.err.println("Status code: " + ex.getStatusCode());
        System.err.println("Description: " + ex.getStatusText());
        System.err.println("Check your bot token etc. and try again.");
        System.err.println("---------------------------");
    }

    public void handleWebClientRequestException(WebClientRequestException ex) {
        System.err.println("---------------------------");
        System.err.println("Error in Polling Thread:");
        System.err.println("Location: " + printLocation());
        System.err.println("Can't connect to Telegram, check your internet connection.");
        System.err.println("Trying to reconnect...");
        System.err.println("---------------------------");
        sleep(TRYING_TO_RECONNECT_DELAY);
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Update> getUpdates() {
        String pollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES.getPath();

        UpdateResponse<Update> response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();

        if (response.getResult().isEmpty())
            return null;

        int offset = response.getResult().get(response.getResult().size() - 1).getUpdateId() + 1;

        String deletePollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES_OFFSET.getPath() + offset;


        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
        return response.getResult();
    }

    public void resetUpdates() throws BotSendException {
        String pollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES.getPath();

        UpdateResponse<Update> response = client
                .get()
                .uri(pollUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UpdateResponse<Update>>() {
                })
                .block();

        if (response.getResult().isEmpty())
            return;

        int offset = ((Update) response.getResult().get(response.getResult().size() - 1)).getUpdateId() + 1;

        String deletePollUrl = BotInfo.GET_URL() + EndPoint.GET_UPDATES_OFFSET.getPath() + offset;

        client.get()
                .uri(deletePollUrl)
                .retrieve()
                .bodyToMono(UpdateResponse.class)
                .block();
    }
}
