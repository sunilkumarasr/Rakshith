package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class CategoryWiseModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<CategoryWiseResponse>,
    @SerializedName("code") val code: Int
)

data class CategoryWiseResponse(
    @SerializedName("products_id") val productsId : String,
    @SerializedName("product_num") val productNum : String,
    @SerializedName("product_name") val productName : String,
    @SerializedName("product_image") val productImage : String,
    @SerializedName("quantity") val quantity : String,
    @SerializedName("stock") val stock : String,
    @SerializedName("sales_price") val salesPrice : String,
    @SerializedName("offer_price") val offerPrice : String,
    @SerializedName("final_amount") val finalAmount : String,
    @SerializedName("cart_id") var cartId : String,
    @SerializedName("category_2_price") val category2Price : String,
    @SerializedName("location_id") val locationIds: List<String>
)