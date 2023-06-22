package org.example.controller;

import org.example.api_object.RequestResponse;
import org.example.api_request.*;
import org.example.configuration.BotInfo;
import org.example.configuration.ParameterizedTypeReferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
public class PostController {
    private final WebClient client;
    private final ParameterizedTypeReferences typeReferences;

    @Autowired
    public PostController(WebClient client,
                          ParameterizedTypeReferences typeReferences) {
        this.client = client;
        this.typeReferences = typeReferences;
    }

    public <Request extends ApiRequest<? extends ReturnObject>, ReturnObject> ReturnObject sendRequest(Request request) {
        request.checkValidation();
        String url = BotInfo.GET_URL() + request.getEndPoint();

        RequestResponse response = (RequestResponse) client
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(typeReferences.get(request.getReturnObject().getClass()))
                .block();
        return (ReturnObject) response.getResult();
    }


    public static boolean isFileIdExist(String fileId) {
        return true;
    }
}
