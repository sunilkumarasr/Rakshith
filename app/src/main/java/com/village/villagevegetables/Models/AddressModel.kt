package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class AddressModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<AddressModelResponse>,
    @SerializedName("code") val code: Int
)

data class AddressModelResponse(
    @SerializedName("id") val id : String,
    @SerializedName("customer_id") val customerId : String,
    @SerializedName("name") val name : String,
    @SerializedName("mobile_no") val mobileNo : String,
    @SerializedName("alternate_mobile_number") val alternateMobileNumber : String,
    @SerializedName("landmark") val landmark : String,
    @SerializedName("city") val city : String,
    @SerializedName("area") val area : String,
    @SerializedName("status") val status : String,
    @SerializedName("created_date") val createdDate : String,
)