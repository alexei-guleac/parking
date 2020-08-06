# StatisticsRecord

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **Long** | Statistics record unique id |  [optional]
**lotNumber** | **Integer** | Statistics record lot number |  [optional]
**status** | [**StatusEnum**](#StatusEnum) | Statistics record lot status |  [optional]
**updatedAt** | [**OffsetDateTime**](OffsetDateTime.md) | Statistics record date |  [optional]

<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----
FREE | &quot;FREE&quot;
OCCUPIED | &quot;OCCUPIED&quot;
RESERVED | &quot;RESERVED&quot;
UNKNOWN | &quot;UNKNOWN&quot;
