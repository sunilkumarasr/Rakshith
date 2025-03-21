package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class PlaceorderModel(
    @SerializedName("Status") val status : Boolean,
    @SerializedName("Message") val message : String,
    @SerializedName("customer_status") var customer_status : String,
    @SerializedName("Response") val response : PlaceOrderResponse,
    @SerializedName("code") val code : Int
)

data class PlaceOrderResponse(
    @SerializedName("order_id") val orderId : Int,
    @SerializedName("amount") val amount : Int
)