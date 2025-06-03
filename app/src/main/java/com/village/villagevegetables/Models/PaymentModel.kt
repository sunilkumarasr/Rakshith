package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class PaymentModel(
    @SerializedName("Status") val status: Boolean,
)
