package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class AreaModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<AreaModelResponse>,
    @SerializedName("code") val code: Int
)

data class AreaModelResponse(
    @SerializedName("city_id") val areaId : String,
    @SerializedName("city_name") val areaName : String,
    @SerializedName("status") val status : String
)
