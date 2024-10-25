package com.royalit.rakshith.Api

import com.royalit.rakshith.Adapters.FaqModel
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


interface ApiInterface {

    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

}