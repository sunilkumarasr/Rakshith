package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class AddAddressModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
)
