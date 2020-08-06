# StatisticsControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAllStatisticsUsingGET**](StatisticsControllerApi.md#getAllStatisticsUsingGET) | **GET** /statistics | Get all statistics records from database
[**getStatisticsByLotNumberUsingGET**](StatisticsControllerApi.md#getStatisticsByLotNumberUsingGET) | **GET** /lot_statistics/{lotNumber} | Get all statistics records by parking lot number from database

<a name="getAllStatisticsUsingGET"></a>
# **getAllStatisticsUsingGET**
> List&lt;StatisticsRecord&gt; getAllStatisticsUsingGET()

Get all statistics records from database

Returns all statistics records from storage

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.StatisticsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

StatisticsControllerApi apiInstance = new StatisticsControllerApi();
try {
    List<StatisticsRecord> result = apiInstance.getAllStatisticsUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatisticsControllerApi#getAllStatisticsUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;StatisticsRecord&gt;**](StatisticsRecord.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getStatisticsByLotNumberUsingGET"></a>
# **getStatisticsByLotNumberUsingGET**
> List&lt;StatisticsRecord&gt; getStatisticsByLotNumberUsingGET(lotNumber)

Get all statistics records by parking lot number from database

Returns all statistics records by parking lot number from storage

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.StatisticsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

StatisticsControllerApi apiInstance = new StatisticsControllerApi();
String lotNumber = "lotNumber_example"; // String | Pass parking lot number
try {
    List<StatisticsRecord> result = apiInstance.getStatisticsByLotNumberUsingGET(lotNumber);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling StatisticsControllerApi#getStatisticsByLotNumberUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **lotNumber** | **String**| Pass parking lot number |

### Return type

[**List&lt;StatisticsRecord&gt;**](StatisticsRecord.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

