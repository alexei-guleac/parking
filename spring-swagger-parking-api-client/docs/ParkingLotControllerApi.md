# ParkingLotControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**cancelReservationUsingDELETE**](ParkingLotControllerApi.md#cancelReservationUsingDELETE) | **DELETE** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingGET**](ParkingLotControllerApi.md#cancelReservationUsingGET) | **GET** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingHEAD**](ParkingLotControllerApi.md#cancelReservationUsingHEAD) | **HEAD** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingOPTIONS**](ParkingLotControllerApi.md#cancelReservationUsingOPTIONS) | **OPTIONS** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingPATCH**](ParkingLotControllerApi.md#cancelReservationUsingPATCH) | **PATCH** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingPOST**](ParkingLotControllerApi.md#cancelReservationUsingPOST) | **POST** /unreserve/{id} | Used to unreserve parking lot
[**cancelReservationUsingPUT**](ParkingLotControllerApi.md#cancelReservationUsingPUT) | **PUT** /unreserve/{id} | Used to unreserve parking lot
[**getAllParkingLotsUsingGET**](ParkingLotControllerApi.md#getAllParkingLotsUsingGET) | **GET** /parking | Get all parking lots
[**getParkingLotByIdUsingGET**](ParkingLotControllerApi.md#getParkingLotByIdUsingGET) | **GET** /parking/{id} | Get parking lot by id
[**reservationUsingDELETE**](ParkingLotControllerApi.md#reservationUsingDELETE) | **DELETE** /reserve/{id} | Used to reserve parking lot
[**reservationUsingGET**](ParkingLotControllerApi.md#reservationUsingGET) | **GET** /reserve/{id} | Used to reserve parking lot
[**reservationUsingHEAD**](ParkingLotControllerApi.md#reservationUsingHEAD) | **HEAD** /reserve/{id} | Used to reserve parking lot
[**reservationUsingOPTIONS**](ParkingLotControllerApi.md#reservationUsingOPTIONS) | **OPTIONS** /reserve/{id} | Used to reserve parking lot
[**reservationUsingPATCH**](ParkingLotControllerApi.md#reservationUsingPATCH) | **PATCH** /reserve/{id} | Used to reserve parking lot
[**reservationUsingPOST**](ParkingLotControllerApi.md#reservationUsingPOST) | **POST** /reserve/{id} | Used to reserve parking lot
[**reservationUsingPUT**](ParkingLotControllerApi.md#reservationUsingPUT) | **PUT** /reserve/{id} | Used to reserve parking lot

<a name="cancelReservationUsingDELETE"></a>
# **cancelReservationUsingDELETE**
> Boolean cancelReservationUsingDELETE(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingDELETE(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingGET"></a>
# **cancelReservationUsingGET**
> Boolean cancelReservationUsingGET(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingGET(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingHEAD"></a>
# **cancelReservationUsingHEAD**
> Boolean cancelReservationUsingHEAD(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingHEAD(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingHEAD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingOPTIONS"></a>
# **cancelReservationUsingOPTIONS**
> Boolean cancelReservationUsingOPTIONS(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingOPTIONS(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingOPTIONS");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingPATCH"></a>
# **cancelReservationUsingPATCH**
> Boolean cancelReservationUsingPATCH(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingPATCH(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingPATCH");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingPOST"></a>
# **cancelReservationUsingPOST**
> Boolean cancelReservationUsingPOST(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingPOST(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="cancelReservationUsingPUT"></a>
# **cancelReservationUsingPUT**
> Boolean cancelReservationUsingPUT(id)

Used to unreserve parking lot

Sets status of parking lot to unreserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.cancelReservationUsingPUT(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#cancelReservationUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getAllParkingLotsUsingGET"></a>
# **getAllParkingLotsUsingGET**
> List&lt;ParkingLot&gt; getAllParkingLotsUsingGET()

Get all parking lots

Returns all parking lots from parking lots storage

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
try {
    List<ParkingLot> result = apiInstance.getAllParkingLotsUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#getAllParkingLotsUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;ParkingLot&gt;**](ParkingLot.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getParkingLotByIdUsingGET"></a>
# **getParkingLotByIdUsingGET**
> ParkingLot getParkingLotByIdUsingGET(headers, id)

Get parking lot by id

Returns parking lot from storage by given id

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Object headers = null; // Object | headers
Long id = 789L; // Long | id
try {
    ParkingLot result = apiInstance.getParkingLotByIdUsingGET(headers, id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#getParkingLotByIdUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |
 **id** | **Long**| id |

### Return type

[**ParkingLot**](ParkingLot.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingDELETE"></a>
# **reservationUsingDELETE**
> Boolean reservationUsingDELETE(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingDELETE(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingGET"></a>
# **reservationUsingGET**
> Boolean reservationUsingGET(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingGET(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingHEAD"></a>
# **reservationUsingHEAD**
> Boolean reservationUsingHEAD(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingHEAD(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingHEAD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingOPTIONS"></a>
# **reservationUsingOPTIONS**
> Boolean reservationUsingOPTIONS(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingOPTIONS(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingOPTIONS");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingPATCH"></a>
# **reservationUsingPATCH**
> Boolean reservationUsingPATCH(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingPATCH(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingPATCH");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingPOST"></a>
# **reservationUsingPOST**
> Boolean reservationUsingPOST(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingPOST(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="reservationUsingPUT"></a>
# **reservationUsingPUT**
> Boolean reservationUsingPUT(id)

Used to reserve parking lot

Sets status of parking lot to reserved

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.ParkingLotControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

ParkingLotControllerApi apiInstance = new ParkingLotControllerApi();
Long id = 789L; // Long | id
try {
    Boolean result = apiInstance.reservationUsingPUT(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ParkingLotControllerApi#reservationUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Long**| id |

### Return type

**Boolean**

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

