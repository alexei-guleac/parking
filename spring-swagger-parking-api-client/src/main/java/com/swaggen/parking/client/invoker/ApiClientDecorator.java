package com.swaggen.parking.client.invoker;

import com.swaggen.parking.client.invoker.utils.UriUtils;
import com.swaggen.parking.client.model.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.WILDCARD;


@Component
public class ApiClientDecorator {

    private final ApiClient apiClient;

    private final UriUtils uriUtils;

    @Autowired
    public ApiClientDecorator(ApiClient apiClient, UriUtils uriUtils) {
        this.apiClient = apiClient;
        this.uriUtils = uriUtils;
    }

    public ResponseEntity doGetJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doPutJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doPatchJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.PATCH, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doPostJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doHeadJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.HEAD, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doDeleteJsonRequest(Object headers, Object postBody, String pathRaw, String[] accepts, String[] contentTypes) {

        String path = uriUtils.buildPostPath(pathRaw);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        if (headers != null)
            headerParams.add("headers", apiClient.parameterToString(headers));

        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<ResponseEntity> returnType = new ParameterizedTypeReference<ResponseEntity>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    public ResponseEntity doGenericGetJsonRequest(Object headers, Object postBody, String pathRaw) {
        return doGetJsonRequest(headers, postBody, pathRaw, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }

    public ResponseEntity doGenericPostJsonRequest(Object headers, Object postBody, String pathRaw) {
        return doPostJsonRequest(headers, postBody, pathRaw, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }

    public ResponseEntity doGenericPutJsonRequest(Object headers, Object postBody, String pathRaw) {
        return doPutJsonRequest(headers, postBody, pathRaw, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }

    public ResponseEntity doGenericPatchJsonRequest(Object headers, Object postBody, String pathRaw) {
        return doPatchJsonRequest(headers, postBody, pathRaw, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }

    public ResponseEntity doGenericHeadJsonRequest(Object headers, Object postBody, String pathRaw) {
        return doHeadJsonRequest(headers, postBody, pathRaw, new String[]{WILDCARD}, new String[]{APPLICATION_JSON});
    }
}
