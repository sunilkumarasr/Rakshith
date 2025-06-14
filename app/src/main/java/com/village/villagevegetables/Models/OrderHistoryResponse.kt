package com.village.villagevegetables.Models

import com.google.gson.annotations.SerializedName

data class OrderHistoryModel(
    @SerializedName("Status") val status : Boolean,
    @SerializedName("Message") val message : String,
    @SerializedName("Response") val response : List<OrderHistoryResponse>,
    @SerializedName("code") val code : Int
)

data class OrderHistoryResponse(
    @SerializedName("order_id") val orderId : String,
    @SerializedName("order_from") val orderFrom : String,
    @SerializedName("order_number") val orderNumber : String,
    @SerializedName("agent_id") val agentId : String,
    @SerializedName("customer_id") val customerId : String,
    @SerializedName("full_name") val fullName : String,
    @SerializedName("email") val email : String,
    @SerializedName("mobile") val mobile : String,
    @SerializedName("product_details") val productDetails : List<OrderHistoryProductDetailsResponse>,
    @SerializedName("billing_address") val billingAddress : String,
    @SerializedName("area_name") val areaName : String,
    @SerializedName("city_name") val cityName : String,
    @SerializedName("grand_total") val grandTotal : String,
    @SerializedName("delivery_charge") val deliveryCharge : String,
    @SerializedName("promocode") val promocode : String,
    @SerializedName("order_notes") val orderNotes : String,
    @SerializedName("delivery_status") val deliveryStatus : String,
    @SerializedName("completed_date") val completedDate : String,
    @SerializedName("created_date") val createdDate : String,
    @SerializedName("order_status") val orderStatus : String,
    @SerializedName("updated_date") val updatedDate : String
)

data class OrderHistoryProductDetailsResponse(
    @SerializedName("products_id") val productsId : String,
    @SerializedName("product_name") val productName : String,
    @SerializedName("qty") val qty : String,
    @SerializedName("pqty") val pqty : String,
    @SerializedName("price") val price : String,
    @SerializedName("image") val image : String
)