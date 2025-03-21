package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class FavouriteModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: MutableList<FavouriteResponse>,
    @SerializedName("code") val code: Int
)

data class FavouriteResponse(
    @SerializedName("id") val id: String,
    @SerializedName("product_id") val productsId: String,
    @SerializedName("product_num") val productNum: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("product_information") val productInformation: String,
    @SerializedName("product_image") val productImage: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("sales_price") val salesPrice: String,
    @SerializedName("offer_price") val offerPrice: String,
)