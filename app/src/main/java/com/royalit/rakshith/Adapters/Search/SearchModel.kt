package com.royalit.rakshith.Adapters.Search

import com.google.gson.annotations.SerializedName
import com.royalit.rakshith.Adapters.Cart.CartItems

data class SearchModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val responsecartList: List<SearchItems>?,
    @SerializedName("code") val code: Int,
)

data class SearchItems(
    @SerializedName("products_id") val products_id: String,
    @SerializedName("categories_id") val categories_id: String,
    @SerializedName("product_num") val product_num: String,
    @SerializedName("product_title") val product_title: String,
    @SerializedName("product_information") val product_information: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("max_order_quantity") val max_order_quantity: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("sales_price") val sales_price: String,
    @SerializedName("offer_price") val offer_price: String,
    @SerializedName("category_2_price") val category_2_price: String,
    @SerializedName("stock") val stock: String,
    @SerializedName("status") val status: String,
    @SerializedName("order_by") val order_by: String,
    @SerializedName("tax_number") val tax_number: String,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("batch_name") val batch_name: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String,
    @SerializedName("final_amount") val final_amount: String,
)