package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class AboutUsModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val aboutUsModelResponse: List<AboutUsModelResponse>,
    @SerializedName("code") val code: Int,
)
data class AboutUsModelResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)