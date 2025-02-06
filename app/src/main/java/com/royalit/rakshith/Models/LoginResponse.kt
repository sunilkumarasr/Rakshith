package com.royalit.rakshith.Models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("Response") val response: LoginResponse,
    @SerializedName("code") val code: Int
)
data class LoginResponse(
    @SerializedName("customer_id") val customer_id: Int,
    @SerializedName("customer_category") val customer_category: Int,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("mobile_number") val mobile_number: String,
    @SerializedName("address") val address: String,
    @SerializedName("email_id") val email_id: String,
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
)