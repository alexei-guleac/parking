# RegistrationControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**registrationUsingPOST**](RegistrationControllerApi.md#registrationUsingPOST) | **POST** /register | User registration in system
[**socialRegistrationUsingDELETE**](RegistrationControllerApi.md#socialRegistrationUsingDELETE) | **DELETE** /register/social | User registration in system by social provider information
[**socialRegistrationUsingGET**](RegistrationControllerApi.md#socialRegistrationUsingGET) | **GET** /register/social | User registration in system by social provider information
[**socialRegistrationUsingHEAD**](RegistrationControllerApi.md#socialRegistrationUsingHEAD) | **HEAD** /register/social | User registration in system by social provider information
[**socialRegistrationUsingOPTIONS**](RegistrationControllerApi.md#socialRegistrationUsingOPTIONS) | **OPTIONS** /register/social | User registration in system by social provider information
[**socialRegistrationUsingPATCH**](RegistrationControllerApi.md#socialRegistrationUsingPATCH) | **PATCH** /register/social | User registration in system by social provider information
[**socialRegistrationUsingPOST**](RegistrationControllerApi.md#socialRegistrationUsingPOST) | **POST** /register/social | User registration in system by social provider information
[**socialRegistrationUsingPUT**](RegistrationControllerApi.md#socialRegistrationUsingPUT) | **PUT** /register/social | User registration in system by social provider information

<a name="registrationUsingPOST"></a>
# **registrationUsingPOST**
> ResponseEntity registrationUsingPOST(body, headers)

User registration in system

Handles user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
RegistrationRequest body = new RegistrationRequest(); // RegistrationRequest | Pass user registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.registrationUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#registrationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**RegistrationRequest**](RegistrationRequest.md)| Pass user registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialRegistrationUsingDELETE"></a>
# **socialRegistrationUsingDELETE**
> ResponseEntity socialRegistrationUsingDELETE(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingDELETE(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="socialRegistrationUsingGET"></a>
# **socialRegistrationUsingGET**
> ResponseEntity socialRegistrationUsingGET(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingGET(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="socialRegistrationUsingHEAD"></a>
# **socialRegistrationUsingHEAD**
> ResponseEntity socialRegistrationUsingHEAD(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingHEAD(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingHEAD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialRegistrationUsingOPTIONS"></a>
# **socialRegistrationUsingOPTIONS**
> ResponseEntity socialRegistrationUsingOPTIONS(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingOPTIONS(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingOPTIONS");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialRegistrationUsingPATCH"></a>
# **socialRegistrationUsingPATCH**
> ResponseEntity socialRegistrationUsingPATCH(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingPATCH(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingPATCH");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialRegistrationUsingPOST"></a>
# **socialRegistrationUsingPOST**
> ResponseEntity socialRegistrationUsingPOST(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="socialRegistrationUsingPUT"></a>
# **socialRegistrationUsingPUT**
> ResponseEntity socialRegistrationUsingPUT(body, headers)

User registration in system by social provider information

Handles social user registration request from client web application

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.RegistrationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

RegistrationControllerApi apiInstance = new RegistrationControllerApi();
SocialRegisterRequest body = new SocialRegisterRequest(); // SocialRegisterRequest | Pass user social registration information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.socialRegistrationUsingPUT(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RegistrationControllerApi#socialRegistrationUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SocialRegisterRequest**](SocialRegisterRequest.md)| Pass user social registration information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

