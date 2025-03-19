package com.village.villagevegetables.Adapters

import com.google.gson.annotations.SerializedName

data class ProductDetailsModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response :ProductDetailsResponse,
    @SerializedName("code") val code: Int,
)

data class ProductDetailsResponse(
    @SerializedName("products_id") val products_id : String,
    @SerializedName("categories_id") val categories_id : String,
    @SerializedName("product_num") val product_num : String,
    @SerializedName("product_name") val product_name : String,
    @SerializedName("product_information") val product_information : String,
    @SerializedName("product_image") val product_image : String,
    @SerializedName("quantity") val quantity : String,
    @SerializedName("sales_price") val sales_price : String,
    @SerializedName("offer_price") val offer_price : String,
    @SerializedName("stock") val stock : String,
    @SerializedName("status") val status : String,
    @SerializedName("order_by") val order_by : String,
    @SerializedName("created_date") val created_date : String,
    @SerializedName("updated_date") val updated_date : String,
    @SerializedName("final_amount") val final_amount : String,
    @SerializedName("images") val productImage: List<ProductImage>,
    @SerializedName("category_2_price") val category_2_price: String
)

data class ProductImage(
    @SerializedName("products_id") val products_id : String,
    @SerializedName("id") val id : String,
    @SerializedName("image") val image : String,
    @SerializedName("fullPath") val fullPath : String,
)