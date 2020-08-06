# AccountControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**confirmUserAccountUsingPOST**](AccountControllerApi.md#confirmUserAccountUsingPOST) | **POST** /confirm_account | Confirm user account in the system.
[**forgotUserPasswordUsingPOST**](AccountControllerApi.md#forgotUserPasswordUsingPOST) | **POST** /forgot_password | User forgot password handler
[**getRecaptchaUsingDELETE**](AccountControllerApi.md#getRecaptchaUsingDELETE) | **DELETE** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingGET**](AccountControllerApi.md#getRecaptchaUsingGET) | **GET** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingHEAD**](AccountControllerApi.md#getRecaptchaUsingHEAD) | **HEAD** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingOPTIONS**](AccountControllerApi.md#getRecaptchaUsingOPTIONS) | **OPTIONS** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingPATCH**](AccountControllerApi.md#getRecaptchaUsingPATCH) | **PATCH** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingPOST**](AccountControllerApi.md#getRecaptchaUsingPOST) | **POST** /validate_captcha | ${AccountController.getRecaptcha.value}
[**getRecaptchaUsingPUT**](AccountControllerApi.md#getRecaptchaUsingPUT) | **PUT** /validate_captcha | ${AccountController.getRecaptcha.value}
[**resetUserPasswordUsingPOST**](AccountControllerApi.md#resetUserPasswordUsingPOST) | **POST** /reset_password | Endpoint to reset a user&#x27;s password

<a name="confirmUserAccountUsingPOST"></a>
# **confirmUserAccountUsingPOST**
> ResponseEntity confirmUserAccountUsingPOST(body, headers)

Confirm user account in the system.

This REST web service method will set user account to ENABLED if passed valid confirmation token.

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | Pass user account confirmation token.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.confirmUserAccountUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#confirmUserAccountUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| Pass user account confirmation token. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="forgotUserPasswordUsingPOST"></a>
# **forgotUserPasswordUsingPOST**
> ResponseEntity forgotUserPasswordUsingPOST(body, headers)

User forgot password handler

Receives the user address and send an reset password email

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
ForgotPassRequest body = new ForgotPassRequest(); // ForgotPassRequest | Pass user forgot password information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.forgotUserPasswordUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#forgotUserPasswordUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ForgotPassRequest**](ForgotPassRequest.md)| Pass user forgot password information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getRecaptchaUsingDELETE"></a>
# **getRecaptchaUsingDELETE**
> ResponseEntity getRecaptchaUsingDELETE(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingDELETE(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="getRecaptchaUsingGET"></a>
# **getRecaptchaUsingGET**
> ResponseEntity getRecaptchaUsingGET(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingGET(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: */*

<a name="getRecaptchaUsingHEAD"></a>
# **getRecaptchaUsingHEAD**
> ResponseEntity getRecaptchaUsingHEAD(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingHEAD(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingHEAD");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getRecaptchaUsingOPTIONS"></a>
# **getRecaptchaUsingOPTIONS**
> ResponseEntity getRecaptchaUsingOPTIONS(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingOPTIONS(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingOPTIONS");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getRecaptchaUsingPATCH"></a>
# **getRecaptchaUsingPATCH**
> ResponseEntity getRecaptchaUsingPATCH(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingPATCH(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingPATCH");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getRecaptchaUsingPOST"></a>
# **getRecaptchaUsingPOST**
> ResponseEntity getRecaptchaUsingPOST(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getRecaptchaUsingPUT"></a>
# **getRecaptchaUsingPUT**
> ResponseEntity getRecaptchaUsingPUT(body, headers)

${AccountController.getRecaptcha.value}

${AccountController.getRecaptcha.notes}

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
String body = "body_example"; // String | gRecaptcha
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.getRecaptchaUsingPUT(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#getRecaptchaUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**String**](String.md)| gRecaptcha |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="resetUserPasswordUsingPOST"></a>
# **resetUserPasswordUsingPOST**
> ResponseEntity resetUserPasswordUsingPOST(body, headers)

Endpoint to reset a user&#x27;s password

Reset user password based on received confirmation token

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.AccountControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

AccountControllerApi apiInstance = new AccountControllerApi();
ResetPasswordRequest body = new ResetPasswordRequest(); // ResetPasswordRequest | Pass user reset password information.
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.resetUserPasswordUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AccountControllerApi#resetUserPasswordUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ResetPasswordRequest**](ResetPasswordRequest.md)| Pass user reset password information. |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

