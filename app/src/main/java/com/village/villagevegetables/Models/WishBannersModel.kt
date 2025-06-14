package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class WishBannersModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: ArrayList<WishBannersResponse>,
    @SerializedName("code") val code: Int
)

data class WishBannersResponse(
    @SerializedName("id") val id : String,
    @SerializedName("image") val image : String,
    @SerializedName("full_path") val fullPath : String
)