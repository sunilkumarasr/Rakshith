package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class PrivacyPolicyModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val privcypolicyResponse: List<PrivacyPolicyResponse>?,
    @SerializedName("code") val code: Int,
)
data class PrivacyPolicyResponse(
    @SerializedName("privacy") val privacy: String,
)