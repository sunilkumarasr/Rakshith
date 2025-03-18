package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class RefundPolicyModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val refundPolicyResponse: List<RefundPolicyResponse>,
    @SerializedName("code") val code: Int,
)
data class RefundPolicyResponse(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
)