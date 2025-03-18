package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class CityModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<CityModelResponse>,
    @SerializedName("code") val code: Int
)

data class CityModelResponse(
    @SerializedName("state_id") val cityId : String,
    @SerializedName("state_name") val cityName : String,
    @SerializedName("status") val status : String
)