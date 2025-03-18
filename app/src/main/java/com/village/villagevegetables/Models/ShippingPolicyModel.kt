package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class ShippingPolicyModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val shippingPolicyResponse: List<ShippingPolicyResponse>,
    @SerializedName("code") val code: Int,
)
data class ShippingPolicyResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)
