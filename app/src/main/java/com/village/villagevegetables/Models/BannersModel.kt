package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class BannersModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: ArrayList<BannersResponse>,
    @SerializedName("code") val code: Int
)

data class BannersResponse(
    @SerializedName("id") val id : String,
    @SerializedName("image") val image : String,
    @SerializedName("url") val url : String,
    @SerializedName("full_path") val fullPath : String
)