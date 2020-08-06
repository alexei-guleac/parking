# UserLdap

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accountState** | [**AccountStateEnum**](#AccountStateEnum) | User account state (enabled, disabled, waiting confirmation) |  [optional]
**cn** | **String** | User full name |  [optional]
**creationDate** | [**OffsetDateTime**](OffsetDateTime.md) | User account creation date |  [optional]
**email** | **String** | User email |  [optional]
**firstname** | **String** |  |  [optional]
**passwordUpdatedAt** | [**OffsetDateTime**](OffsetDateTime.md) | User password last updated at date |  [optional]
**sn** | **String** | User lastname |  [optional]
**socialIds** | **Map&lt;String, String&gt;** | Map of user social id&#x27;s by social service providers |  [optional]
**uid** | **String** | User server uid |  [optional]
**updatedAt** | [**OffsetDateTime**](OffsetDateTime.md) | User account last updated at date |  [optional]
**userPassword** | **String** | User password |  [optional]

<a name="AccountStateEnum"></a>
## Enum: AccountStateEnum
Name | Value
---- | -----
WAITING_CONFIRMATION | &quot;WAITING_CONFIRMATION&quot;
ENABLED | &quot;ENABLED&quot;
DISABLED | &quot;DISABLED&quot;
SOCIAL | &quot;SOCIAL&quot;
