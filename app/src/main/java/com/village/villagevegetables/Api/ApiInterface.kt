package com.village.villagevegetables.Api

import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Adapters.FaqModel
import com.village.villagevegetables.Adapters.ProductDetailsModel
import com.village.villagevegetables.Adapters.Search.SearchModel
import com.village.villagevegetables.Models.AboutUsModel
import com.village.villagevegetables.Models.AddAddressModel
import com.village.villagevegetables.Models.AddFavouriteModel
import com.village.villagevegetables.Models.AddressModel
import com.village.villagevegetables.Models.AddtoCartResponse
import com.village.villagevegetables.Models.AreaModel
import com.village.villagevegetables.Models.BannersModel
import com.village.villagevegetables.Models.CategoryModel
import com.village.villagevegetables.Models.CategoryWiseModel
import com.village.villagevegetables.Models.CityModel
import com.village.villagevegetables.Models.ContactUsModel
import com.village.villagevegetables.Models.DeleteCartResponse
import com.village.villagevegetables.Models.DeleteModel
import com.village.villagevegetables.Models.FavouriteModel
import com.village.villagevegetables.Models.ForgotModel
import com.village.villagevegetables.Models.LoginModel
import com.village.villagevegetables.Models.OrderHistoryModel
import com.village.villagevegetables.Models.PlaceorderModel
import com.village.villagevegetables.Models.PrivacyPolicyModel
import com.village.villagevegetables.Models.ProductModel
import com.village.villagevegetables.Models.ProfileModel
import com.village.villagevegetables.Models.RefundPolicyModel
import com.village.villagevegetables.Models.RegisterModel
import com.village.villagevegetables.Models.SettingsModel
import com.village.villagevegetables.Models.ShippingPolicyModel
import com.village.villagevegetables.Models.TermsAndConditionsModel
import com.village.villagevegetables.Models.UpdateCartResponse
import retrofit2.http.GET
import retrofit2.http.POST
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
        @Field("pswrd") password: String,
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
    @POST("settings")
    fun settingsApi(
        @Field("api_key") apiKey: String,
    ): Call<SettingsModel>

    @FormUrlEncoded
    @POST("sliders")
    fun getBannersApi(
        @Field("api_key") apiKey: String
    ): Call<BannersModel>

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


    @FormUrlEncoded
    @POST("userbased_favorite")
    fun getFavouriteApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<FavouriteModel>

    @FormUrlEncoded
    @POST("add_favorite")
    fun addFavouriteApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("product_id") productId: String
    ): Call<AddFavouriteModel>

    @FormUrlEncoded
    @POST("delete_favorite")
    fun removeFavouriteApi(
        @Field("api_key") apiKey: String,
        @Field("id") id: String
    ): Call<AddFavouriteModel>

    @FormUrlEncoded
    @POST("user_get_profile")
    fun getProfileApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<ProfileModel>

    @FormUrlEncoded
    @POST("update_profile")
    fun updateProfileApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("full_name") fullName: String,
        @Field("mobile_number") mobileNumber: String,
        @Field("email_id") emailId: String,
    ): Call<ProfileModel>

    @FormUrlEncoded
    @POST("get_orders_list")
    fun getOrdersHistoryApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<OrderHistoryModel>

    @FormUrlEncoded
    @POST("state")
    fun getCityListApi(
        @Field("api_key") apiKey: String
    ): Call<CityModel>

    @FormUrlEncoded
    @POST("city")
    fun getAreaListApi(
        @Field("api_key") apiKey: String,
        @Field("state_id") stateId: String,
    ): Call<AreaModel>

    @FormUrlEncoded
    @POST("add_addressinfo")
    fun addAddressApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("name") name: String,
        @Field("mobile_no") mobileNo: String,
        @Field("alternate_mobile_number") alternateMobileNumber: String,
        @Field("landmark") landmark: String,
        @Field("city") city: String,
        @Field("area") area: String,
    ): Call<AddAddressModel>

    @FormUrlEncoded
    @POST("update_address")
    fun updateAddressApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("address_id") addressId: String,
        @Field("name") name: String,
        @Field("mobile_no") mobileNo: String,
        @Field("alternate_mobile_number") alternateMobileNumber: String,
        @Field("landmark") landmark: String,
        @Field("city") city: String,
        @Field("area") area: String,
    ): Call<AddAddressModel>


    @FormUrlEncoded
    @POST("address_list")
    fun addressListApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
    ): Call<AddressModel>


    @FormUrlEncoded
    @POST("delete_address")
    fun deleteAddressApi(
        @Field("api_key") apiKey: String,
        @Field("address_id") addressId: String
    ): Call<AddressModel>

    
    @FormUrlEncoded
    @POST("place_order_save")
    fun placeOrderSuccessApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
        @Field("product_ids") productIds: String,
        @Field("product_qtys") productQty: String,
        @Field("order_notes") orderNotes: String,
        @Field("amount") amount: String,
        @Field("address") address: String,
        @Field("promocode") promoCode: String,
        @Field("delivery_charge") deliveryCharge: String,
    ): Call<PlaceorderModel>


    @GET("faq")
    fun faqListApi(): Call<List<FaqModel>>

    @FormUrlEncoded
    @POST("terms")
    fun termsAndConditionsApi(
        @Field("api_key") apiKey: String
    ): Call<TermsAndConditionsModel>


    @FormUrlEncoded
    @POST("refundpolicy")
    fun refundPolicyApi(
        @Field("api_key") apiKey: String
    ): Call<RefundPolicyModel>


    @FormUrlEncoded
    @POST("shipping")
    fun shippingPolicyApi(
        @Field("api_key") apiKey: String
    ): Call<ShippingPolicyModel>


    @FormUrlEncoded
    @POST("about")
    fun aboutUsApi(
        @Field("api_key") apiKey: String
    ): Call<AboutUsModel>

    @FormUrlEncoded
    @POST("privacypolicy")
    fun privacyPolicyApi(
        @Field("api_key") apiKey: String
    ): Call<PrivacyPolicyModel>

    @FormUrlEncoded
    @POST("contact")
    fun contactUsApi(
        @Field("api_key") apiKey: String
    ): Call<ContactUsModel>

    @FormUrlEncoded
    @POST("deleteaccount")
    fun deleteAccountApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String,
    ): Call<DeleteModel>

}