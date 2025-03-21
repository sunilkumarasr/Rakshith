package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class SettingsModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<SettingsResponse>,
    @SerializedName("code") val code: Int
)

data class SettingsResponse(
    @SerializedName("app_mode") val appMode : String,
    @SerializedName("cart_text") val cartText : String
)