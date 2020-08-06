# UserControllerApi

All URIs are relative to *//localhost:8080/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteUserUsingPOST**](UserControllerApi.md#deleteUserUsingPOST) | **POST** /profile/delete | Delete user
[**getAllUsersUsingGET**](UserControllerApi.md#getAllUsersUsingGET) | **GET** /users | View a list of available employees
[**getUserByUsernameUsingPOST**](UserControllerApi.md#getUserByUsernameUsingPOST) | **POST** /profile | Get an employee by username
[**updateUserUsingPOST**](UserControllerApi.md#updateUserUsingPOST) | **POST** /profile/update | Update user

<a name="deleteUserUsingPOST"></a>
# **deleteUserUsingPOST**
> ResponseEntity deleteUserUsingPOST(headers)

Delete user

Endpoint to delete user profile from database

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.UserControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

UserControllerApi apiInstance = new UserControllerApi();
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.deleteUserUsingPOST(headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserControllerApi#deleteUserUsingPOST");
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

<a name="getAllUsersUsingGET"></a>
# **getAllUsersUsingGET**
> UserLdap getAllUsersUsingGET()

View a list of available employees

Endpoint to retrieve all users from database

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.UserControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

UserControllerApi apiInstance = new UserControllerApi();
try {
    UserLdap result = apiInstance.getAllUsersUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserControllerApi#getAllUsersUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**UserLdap**](UserLdap.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="getUserByUsernameUsingPOST"></a>
# **getUserByUsernameUsingPOST**
> SocialUser getUserByUsernameUsingPOST()

Get an employee by username

Endpoint to retrieve user profile from database

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.UserControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

UserControllerApi apiInstance = new UserControllerApi();
try {
    SocialUser result = apiInstance.getUserByUsernameUsingPOST();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserControllerApi#getUserByUsernameUsingPOST");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SocialUser**](SocialUser.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="updateUserUsingPOST"></a>
# **updateUserUsingPOST**
> ResponseEntity updateUserUsingPOST(body, headers)

Update user

Endpoint to update user profile in database

### Example
```java
// Import classes:
//import com.swaggen.parking.client.invoker.ApiClient;
//import com.swaggen.parking.client.invoker.ApiException;
//import com.swaggen.parking.client.invoker.Configuration;
//import com.swaggen.parking.client.invoker.auth.*;
//import com.swaggen.parking.client.api.UserControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: JWT
ApiKeyAuth JWT = (ApiKeyAuth) defaultClient.getAuthentication("JWT");
JWT.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//JWT.setApiKeyPrefix("Token");

UserControllerApi apiInstance = new UserControllerApi();
UpdateUserRequest body = new UpdateUserRequest(); // UpdateUserRequest | Pass user information for update
Object headers = null; // Object | headers
try {
    ResponseEntity result = apiInstance.updateUserUsingPOST(body, headers);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling UserControllerApi#updateUserUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UpdateUserRequest**](UpdateUserRequest.md)| Pass user information for update |
 **headers** | [**Object**](.md)| headers |

### Return type

[**ResponseEntity**](ResponseEntity.md)

### Authorization

[JWT](../README.md#JWT)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

