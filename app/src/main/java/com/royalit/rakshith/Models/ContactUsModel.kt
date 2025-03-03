package com.royalit.rakshith.Models

import com.google.gson.annotations.SerializedName

data class ContactUsModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val contactResponse: List<ContactResponse>?,
    @SerializedName("code") val code: Int,
)
data class ContactResponse(
    @SerializedName("mobileno") val mobileno: String,
)
