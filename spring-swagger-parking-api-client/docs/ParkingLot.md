# ParkingLot

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **Long** | Parking lot unique id (two numbers comes from Arduino scalable infrastructure - master board id + slave board id) |  [optional]
**number** | **Integer** | Parking lot number |  [optional]
**status** | [**StatusEnum**](#StatusEnum) | Parking lot status (free, occupied, unknown, reserved) |  [optional]
**updatedAt** | [**OffsetDateTime**](OffsetDateTime.md) | Parking lot updated at date |  [optional]

<a name="StatusEnum"></a>
## Enum: StatusEnum
Name | Value
---- | -----
FREE | &quot;FREE&quot;
OCCUPIED | &quot;OCCUPIED&quot;
RESERVED | &quot;RESERVED&quot;
UNKNOWN | &quot;UNKNOWN&quot;
