package com.swaggen.parking.client.api;

import com.swaggen.parking.client.invoker.ApiClient;
import com.swaggen.parking.client.model.StatisticsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-08-06T11:26:27.336+03:00[EET]")
@Component("com.swaggen.parking.client.api.StatisticsControllerApi")
public class StatisticsControllerApi {
    private ApiClient apiClient;

    public StatisticsControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public StatisticsControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all statistics records from database
     * Returns all statistics records from storage
     * <p><b>200</b> - Success|OK
     *
     * @return List&lt;StatisticsRecord&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<StatisticsRecord> getAllStatisticsUsingGET() throws RestClientException {
        Object postBody = null;
        String path = UriComponentsBuilder.fromPath("/statistics").build().toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {};
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<List<StatisticsRecord>> returnType = new ParameterizedTypeReference<List<StatisticsRecord>>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }

    /**
     * Get all statistics records by parking lot number from database
     * Returns all statistics records by parking lot number from storage
     * <p><b>200</b> - Success|OK
     * <p><b>500</b> - Server error
     *
     * @param lotNumber Pass parking lot number
     * @return List&lt;StatisticsRecord&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<StatisticsRecord> getStatisticsByLotNumberUsingGET(String lotNumber) throws RestClientException {
        Object postBody = null;
        // verify the required parameter 'lotNumber' is set
        if (lotNumber == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'lotNumber' when calling getStatisticsByLotNumberUsingGET");
        }
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("lotNumber", lotNumber);
        String path = UriComponentsBuilder.fromPath("/lot_statistics/{lotNumber}").buildAndExpand(uriVariables).toUriString();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {
                "*/*"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = {};
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[]{"JWT"};

        ParameterizedTypeReference<List<StatisticsRecord>> returnType = new ParameterizedTypeReference<List<StatisticsRecord>>() {
        };
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
