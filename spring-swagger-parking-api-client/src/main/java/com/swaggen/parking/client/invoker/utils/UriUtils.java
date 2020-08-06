package com.swaggen.parking.client.invoker.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class UriUtils {

    public String buildPostPath(String pathRaw) {
        return UriComponentsBuilder.fromPath(pathRaw).build().toUriString();
    }

    public String buildGetPath(String pathRaw, String uriVariables) {
        return UriComponentsBuilder.fromPath(pathRaw).buildAndExpand(uriVariables).toUriString();
    }
}
