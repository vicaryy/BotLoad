package org.example.controller;

import org.example.api_request.InputFile;
import org.example.configuration.ApiBotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;

@Controller
public class PostSender {
    private final RestTemplate restTemplate;
    private final ApiBotConfiguration apiConfiguration;

    @Autowired
    public PostSender(RestTemplate restTemplate,
                      ApiBotConfiguration apiConfiguration) {
        this.restTemplate = restTemplate;
        this.apiConfiguration = apiConfiguration;
    }

    public void execute(Object object) {
        String endPoint = object.getClass().getSimpleName().toLowerCase();
        String url = apiConfiguration.getUrl() + endPoint;

        MultipartBodyBuilder builder1 = new MultipartBodyBuilder();
        builder1.part("audio", new File("/Users/vicary/desktop/test.mp3"));


        WebClient.Builder builder = WebClient.builder();
        String e = builder.build()
                .post()
                .uri(url)
                .bodyValue(object)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(e);
        //restTemplate.postForEntity(url, object, object.getClass());
    }

    public void executeAnimation() {
        String endPoint = "sendAnimation";
        String url = apiConfiguration.getUrl() + endPoint;
        System.out.println(endPoint);
        Integer chat_id = 1935527130;

        File file = new File("/Users/vicary/desktop/nailsing.gif");
        InputFile inputFile = new InputFile("MUZA", file);

        WebClient webClient = WebClient.create();

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("chat_id", chat_id);
        multipartBodyBuilder.part("animation", new FileSystemResource(inputFile.getFile()));
        System.out.println(multipartBodyBuilder.build());
        String response = webClient.post()
                .uri(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        System.out.println(response);
    }
}
