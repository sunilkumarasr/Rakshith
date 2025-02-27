package com.royalit.rakshith.Models

import com.google.gson.annotations.SerializedName

data class DeleteCartResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("code") val code: Int
)
