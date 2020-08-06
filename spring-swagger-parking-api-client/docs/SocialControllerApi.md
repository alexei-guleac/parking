# SocialControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**githubOAuthUsingDELETE**](SocialControllerApi.md#githubOAuthUsingDELETE) | **DELETE** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingGET**](SocialControllerApi.md#githubOAuthUsingGET) | **GET** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingHEAD**](SocialControllerApi.md#githubOAuthUsingHEAD) | **HEAD** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingOPTIONS**](SocialControllerApi.md#githubOAuthUsingOPTIONS) | **OPTIONS** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingPATCH**](SocialControllerApi.md#githubOAuthUsingPATCH) | **PATCH** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingPOST**](SocialControllerApi.md#githubOAuthUsingPOST) | **POST** /github_oauth | Method complements OAuth 2.0 flow with Github
[**githubOAuthUsingPUT**](SocialControllerApi.md#githubOAuthUsingPUT) | **PUT** /github_oauth | Method complements OAuth 2.0 flow with Github
[**socialConnectUsingPOST**](SocialControllerApi.md#socialConnectUsingPOST) | **POST** /social_connect | Connect social provider to user account
[**socialDisconnectUsingPOST**](SocialControllerApi.md#socialDisconnectUsingPOST) | **POST** /social_disconnect | Disconnect social provider from user account

<a name="githubOAuthUsingDELETE"></a>
# **githubOAuthUsingDELETE**
> ResponseEntity githubOAuthUsingDELETE(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingDELETE(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="githubOAuthUsingGET"></a>
# **githubOAuthUsingGET**
> ResponseEntity githubOAuthUsingGET(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingGET(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="githubOAuthUsingHEAD"></a>
# **githubOAuthUsingHEAD**
> ResponseEntity githubOAuthUsingHEAD(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingHEAD(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingHEAD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="githubOAuthUsingOPTIONS"></a>
# **githubOAuthUsingOPTIONS**
> ResponseEntity githubOAuthUsingOPTIONS(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingOPTIONS(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingOPTIONS");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="githubOAuthUsingPATCH"></a>
# **githubOAuthUsingPATCH**
> ResponseEntity githubOAuthUsingPATCH(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingPATCH(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingPATCH");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="githubOAuthUsingPOST"></a>
# **githubOAuthUsingPOST**
> ResponseEntity githubOAuthUsingPOST(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingPOST(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="githubOAuthUsingPUT"></a>
# **githubOAuthUsingPUT**
> ResponseEntity githubOAuthUsingPUT(headers)

Method complements OAuth 2.0 flow with Github

GitHub redirects back to site with a temporary code in a code parameter as well as the state you provided in the previous step in a state parameter. The temporary code will expire after 10 minutes. This code needed for exchange to access token which is needed for app access the Github API

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.githubOAuthUsingPUT(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#githubOAuthUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialConnectUsingPOST"></a>
# **socialConnectUsingPOST**
> ResponseEntity socialConnectUsingPOST(body, headers)

Connect social provider to user account

Handles user social connection request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
SocialConnectRequest body = new SocialConnectRequest(); // SocialConnectRequest | Pass user and social provider information
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialConnectUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#socialConnectUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialConnectRequest**](SocialConnectRequest.md)| Pass user and social provider information |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialDisconnectUsingPOST"></a>
# **socialDisconnectUsingPOST**
> ResponseEntity socialDisconnectUsingPOST(body, headers)

Disconnect social provider from user account

Handles user social disconnection request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.SocialControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

SocialControllerApi apiInstance = new SocialControllerApi();
SocialDisconnectRequest body = new SocialDisconnectRequest(); // SocialDisconnectRequest | Pass user and social provider information
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialDisconnectUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SocialControllerApi#socialDisconnectUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialDisconnectRequest**](SocialDisconnectRequest.md)| Pass user and social provider information |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

