package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName
import com.village.villagevegetables.Adapters.Cart.CartItems

data class PromoCodeListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val ResponseCartList: List<PromoCodeItems>,
    @SerializedName("code") val code: Int,
)

data class PromoCodeItems(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String,
)