package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class AddFavouriteModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
)
