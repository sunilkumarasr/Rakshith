package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class CreateOrderModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("order_id") val order_id: String,
)
