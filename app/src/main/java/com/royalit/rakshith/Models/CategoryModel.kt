package com.royalit.rakshith.Models

import com.google.gson.annotations.SerializedName

data class CategoryModel(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val response: List<CategoryListResponse>,
    @SerializedName("code") val code: Int
)

data class CategoryListResponse(
    @SerializedName("categories_id") val categoriesId : String,
    @SerializedName("category_name") val categoryName : String,
    @SerializedName("category_image") val categoryImage : String
)