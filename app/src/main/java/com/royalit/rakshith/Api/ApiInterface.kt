package com.royalit.rakshith.Api

import com.royalit.rakshith.Adapters.FaqModel
import com.royalit.rakshith.Models.ForgotModel
import com.royalit.rakshith.Models.LoginModel
import com.royalit.rakshith.Models.RegisterModel
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
        @Field("api_key") api_key: String,
        @Field("full_name") full_name: String,
        @Field("mobile_number") mobile_number: String,
        @Field("email_id") email_id: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("pswrd") pswrd: String,
        @Field("address") address: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("user_login")
    fun loginApi(
        @Field("api_key") api_key: String,
        @Field("username") username: String,
        @Field("pswrd") pswrd: String,
        @Field("device_token") device_token: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("user_forget_password")
    fun forgotApi(
        @Field("api_key") api_key: String,
        @Field("email_id") username: String
    ): Call<ForgotModel>


    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

}