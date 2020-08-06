# AuthenticationControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**authenticationUsingPOST**](AuthenticationControllerApi.md#authenticationUsingPOST) | **POST** /auth | User authentication in system
[**socialLoginUsingPOST**](AuthenticationControllerApi.md#socialLoginUsingPOST) | **POST** /auth/social | User authentication in system by social provider id

<a name="authenticationUsingPOST"></a>
# **authenticationUsingPOST**
> ResponseEntity authenticationUsingPOST(body, headers)

User authentication in system

Handles user authentication request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AuthenticationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AuthenticationControllerApi apiInstance = new AuthenticationControllerApi();
AuthenticationRequest body = new AuthenticationRequest(); // AuthenticationRequest | Pass user credentials.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.authenticationUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationControllerApi#authenticationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**AuthenticationRequest**](AuthenticationRequest.md)| Pass user credentials. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialLoginUsingPOST"></a>
# **socialLoginUsingPOST**
> ResponseEntity socialLoginUsingPOST(body, headers)

User authentication in system by social provider id

Handles social user authentication request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AuthenticationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AuthenticationControllerApi apiInstance = new AuthenticationControllerApi();
SocialAuthRequest body = new SocialAuthRequest(); // SocialAuthRequest | Account associated with this social profile not found
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialLoginUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationControllerApi#socialLoginUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialAuthRequest**](SocialAuthRequest.md)| Account associated with this social profile not found |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

