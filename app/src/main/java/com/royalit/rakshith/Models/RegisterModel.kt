package com.royalit.rakshith.Models

import com.google.gson.annotations.SerializedName

data class RegisterModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("order_id") val order_id: String,
    @SerializedName("Response") val response: RegisterResponse,
    @SerializedName("code") val code: Int
)
data class RegisterResponse(
    @SerializedName("full_name") val full_name: String,
    @SerializedName("mobile_number") val mobile_number: String,
    @SerializedName("email_id") val email_id: String,
    @SerializedName("password") val password: String,
    @SerializedName("login_create_from") val login_create_from: String,
    @SerializedName("address") val address: String,
    @SerializedName("state") val state: String,
    @SerializedName("city") val city: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("user_id") val user_id: String,
)