package com.isd.parking.services;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Service for provide making REST requests from server to other services
 */
@Service
public class RestService {

    private final RestTemplate restTemplate;

    @Autowired
    public RestService(@NotNull RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public @Nullable String getPlainJSON(@NotNull String url) {
        return this.restTemplate.getForObject(url, String.class);
    }

    public @Nullable String postPlainJSON(@NotNull String url, String postParams) {
        @NotNull HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        @NotNull HttpEntity<String> request =
            new HttpEntity<>(postParams, headers);

        return this.restTemplate.postForObject(url, request, String.class);
    }
}
