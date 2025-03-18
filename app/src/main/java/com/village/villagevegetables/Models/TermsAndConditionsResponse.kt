package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class TermsAndConditionsModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val termsandconditionsResponse: List<TermsAndConditionsResponse>,
    @SerializedName("code") val code: Int,
)
data class TermsAndConditionsResponse(
    @SerializedName("terms") val terms: String,
)
