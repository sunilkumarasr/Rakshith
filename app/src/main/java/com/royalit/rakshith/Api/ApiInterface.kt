package com.royalit.rakshith.Api

import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.FaqModel
import com.royalit.rakshith.Adapters.ProductDetailsModel
import com.royalit.rakshith.Adapters.Search.SearchModel
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.CategoryModel
import com.royalit.rakshith.Models.CategoryWiseModel
import com.royalit.rakshith.Models.ContactUsModel
import com.royalit.rakshith.Models.DeleteCartResponse
import com.royalit.rakshith.Models.ForgotModel
import com.royalit.rakshith.Models.LoginModel
import com.royalit.rakshith.Models.PrivacyPolicyModel
import com.royalit.rakshith.Models.ProductModel
import com.royalit.rakshith.Models.RegisterModel
import com.royalit.rakshith.Models.TermsAndConditionsModel
import com.royalit.rakshith.Models.UpdateCartResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded


interface ApiInterface {

    @FormUrlEncoded
    @POST("user_resistration")
    fun registerApi(
        @Field("api_key") apiKey: String,
        @Field("full_name") fullName: String,
        @Field("mobile_number") mobileNumber: String,
        @Field("email_id") emailId: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("pswrd") password: String,
        @Field("address") address: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("user_login")
    fun loginApi(
        @Field("api_key") apiKey: String,
        @Field("username") username: String,
        @Field("pswrd") password: String,
        @Field("device_token") deviceToken: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("user_forget_password")
    fun forgotApi(
        @Field("api_key") apiKey: String,
        @Field("email_id") username: String
    ): Call<ForgotModel>


    @FormUrlEncoded
    @POST("categories_list")
    fun getCategoriesApi(
        @Field("api_key") apiKey: String
    ): Call<CategoryModel>

    @FormUrlEncoded
    @POST("category_wise_products_list")
    fun getCategoryWiseProductsListApi(
        @Field("api_key") apiKey: String,
        @Field("categories_id") categoriesId: String
    ): Call<CategoryWiseModel>

    @FormUrlEncoded
    @POST("cart_list")
    fun getCartApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<CartListResponse>

    @FormUrlEncoded
    @POST("all_products_list")
    fun getProductsApi(
        @Field("api_key") apiKey: String
    ): Call<ProductModel>


    @FormUrlEncoded
    @POST("search_products_list")
    fun searchApi(
        @Field("api_key") apiKey: String,
        @Field("search_word") searchWord: String
    ): Call<SearchModel>


    @FormUrlEncoded
    @POST("product_wise_indetails_info")
    fun productDetailsApi(
        @Field("api_key") apiKey: String,
        @Field("products_id") productsId: String
    ): Call<ProductDetailsModel>

    @FormUrlEncoded
    @POST("add_cart")
    fun addToCartApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: String,
    ): Call<AddtoCartResponse>


    @FormUrlEncoded
    @POST("update_cart")
    fun upDateCartApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("product_id") productId: String,
        @Field("quantity") quantity: String,
    ): Call<UpdateCartResponse>

    @FormUrlEncoded
    @POST("api/remove_cart")
    fun removeFromCartApi(
        @Field("api_key") api_key: String,
        @Field("customer_id") customer_id: String,
        @Field("product_id") product_id: String,
        @Field("cart_id") cart_id: String,
    ): Call<DeleteCartResponse>

    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

    @FormUrlEncoded
    @POST("terms")
    fun termsAndConditionsApi(
        @Field("api_key") api_key: String
    ): Call<TermsAndConditionsModel>

    @FormUrlEncoded
    @POST("privacy")
    fun privacyPolicyApi(
        @Field("api_key") api_key: String
    ): Call<PrivacyPolicyModel>

    @FormUrlEncoded
    @POST("contact")
    fun contactUsApi(
        @Field("api_key") api_key: String
    ): Call<ContactUsModel>

}