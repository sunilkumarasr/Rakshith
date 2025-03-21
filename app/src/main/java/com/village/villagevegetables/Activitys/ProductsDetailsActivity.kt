package com.village.villagevegetables.Activitys

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Adapters.ProductDetailsModel
import com.village.villagevegetables.Adapters.ProductDetailsResponse
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddFavouriteModel
import com.village.villagevegetables.Models.AddtoCartResponse
import com.village.villagevegetables.Models.DeleteCartResponse
import com.village.villagevegetables.Models.FavouriteModel
import com.village.villagevegetables.Models.UpdateCartResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityProductsDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsDetailsActivity : AppCompatActivity() {

    val binding: ActivityProductsDetailsBinding by lazy {
        ActivityProductsDetailsBinding.inflate(layoutInflater)
    }

    var productsId: String = ""

    var productResponseDetails: ProductDetailsResponse?=null

    lateinit var quantity: IntArray
    var isFavorite = false
    var productCartStatus: String = ""
    var TotalPrice: Double = 0.0

    var favId: String = ""
    var cartId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        productsId = intent.getStringExtra("productsId").toString()
        quantity = intArrayOf(binding.cartQty.text.toString().toInt())

        inIts()

    }

    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.AddFavourite.setOnClickListener {
            val animations = ViewController.animation()
            binding.AddFavourite.startAnimation(animations)
            isFavorite = !isFavorite
            if (isFavorite) {
                addFavouriteApi()
                binding.imgFav.setImageResource(R.drawable.favadd)
            } else {
                removeFavouriteApi()
                binding.imgFav.setImageResource(R.drawable.favorite_green_ic)
            }
        }

        binding.linearIncrement.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearIncrement.startAnimation(animations)
            val cartQty = binding.cartQty.text.toString()
            if(productResponseDetails==null)
            {
                return@setOnClickListener
            }
            if (productResponseDetails?.stock == cartQty) {
                ViewController.customToast(this@ProductsDetailsActivity,"Stock Limit only " + productResponseDetails?.stock)
            }else{
                quantity[0]++
                binding.cartQty.text = quantity[0].toString()
                val cartQty1 = binding.cartQty.text.toString()
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.customToast(applicationContext, "Please check your connection ")
                } else {
                    if (cartQty1 == "1")
                        addToCartApi()
                    else{
                        upDateCartApi(cartQty1)
                    }
                }
            }

        }
        binding.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearDecrement.startAnimation(animations)
            if (quantity[0] > 1) {
                quantity[0]--
                binding.cartQty.text = quantity[0].toString()
                val cartQty1 = binding.cartQty.text.toString()
                upDateCartApi(cartQty1)
            }else{
                removeFromCartApi()
            }
        }

        binding.txtAddToCart.setOnClickListener {
            val animations = ViewController.animation()
            binding.txtAddToCart.startAnimation(animations)

            val cartQty1 = binding.cartQty.text.toString()
            if(productResponseDetails==null)
            {
                return@setOnClickListener
            }
            if (productResponseDetails?.stock == cartQty1) {
                ViewController.customToast(this@ProductsDetailsActivity,"Stock Limit only " + productResponseDetails?.stock)
            }else if (productCartStatus.equals("0")){
                try {
                    quantity[0]++
                    binding.cartQty.text = quantity[0].toString()
                    val cQty = binding.cartQty.text.toString()
                    if (cQty == "1"){
                        addToCartApi()
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
            else {
                val intent = Intent(this@ProductsDetailsActivity, CartActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }

        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            productDetailsApi()
            getFavouriteApi()
        }

    }

    //favorite
    private fun getFavouriteApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        Log.e("userId_",userId.toString())
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getFavouriteApi(
                getString(R.string.api_key),
                userId.toString(),
            )
        call.enqueue(object : Callback<FavouriteModel> {
            override fun onResponse(
                call: Call<FavouriteModel>,
                response: Response<FavouriteModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        val productsList = response.body()?.response!!

                        if (productsList.isNotEmpty()) {
                            for (i in productsList.indices) {
                                // Access each product by index
                                if (productsList[i].productsId == productsId) {
                                    favId = productsList[i].id
                                    isFavorite = true
                                    binding.imgFav.setImageResource(R.drawable.favadd)
                                    break
                                }
                            }
                        }

                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<FavouriteModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun addFavouriteApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        Log.e("userId_",userId.toString())
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.addFavouriteApi(
                getString(R.string.api_key),
                userId.toString(),
                productsId
            )
        call.enqueue(object : Callback<AddFavouriteModel> {
            override fun onResponse(
                call: Call<AddFavouriteModel>,
                response: Response<AddFavouriteModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                    }

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<AddFavouriteModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun removeFavouriteApi() {
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.removeFavouriteApi(
                getString(R.string.api_key),
                favId
            )
        call.enqueue(object : Callback<AddFavouriteModel> {
            override fun onResponse(
                call: Call<AddFavouriteModel>,
                response: Response<AddFavouriteModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<AddFavouriteModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })
    }



    //details api
    private fun productDetailsApi() {
        binding.shimmerLoading.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.productDetailsApi(
                getString(R.string.api_key),
                productsId,
            )
        call.enqueue(object : Callback<ProductDetailsModel> {
            override fun onResponse(
                call: Call<ProductDetailsModel>,
                response: Response<ProductDetailsModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        productResponseDetails = response.body()?.response!!
                        DataSet(productResponseDetails!!)
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    binding.shimmerLoading.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<ProductDetailsModel>, t: Throwable) {
                binding.shimmerLoading.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun DataSet(productResponse: ProductDetailsResponse) {
        getCartApi()

        //out of stock
        if (productResponse.stock.toInt() ==0) {
            binding.linearOutOfStock.visibility = View.VISIBLE
            binding.linearInStock.visibility = View.GONE
            binding.cardBottom.visibility = View.GONE
        } else {
            binding.linearOutOfStock.visibility = View.GONE
            binding.linearInStock.visibility = View.VISIBLE
            binding.cardBottom.visibility = View.VISIBLE
        }

        Glide.with(binding.imgProduct)
            .load(productResponse.product_image)
            .error(R.drawable.logo)
            .into(binding.imgProduct)
        binding.txtTitle.text = productResponse.product_name
        binding.txtOfferPrice.text = "\u20B9" + productResponse.offer_price
        binding.txtActualPrice.text = "\u20B9" + productResponse.sales_price
        binding.txtActualPrice.paintFlags = binding.txtActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.txtQuantity.text = productResponse.quantity
        binding.txtDec.text = HtmlCompat.fromHtml(productResponse.product_information, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    //get cart
    private fun getCartApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getCartApi(
                getString(R.string.api_key),
                userId.toString(),
            )
        call.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                binding.shimmerLoading.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.ResponseCartList?.size!! > 0) {
                            binding.cartQty.text = "0"

                            //default data set
                            if (binding.cartQty.text.toString() == "0"){
                                productCartStatus = "0";
                                binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.addtocart)
                            }
                            //total amount showing
                            val offerPrice = productResponseDetails?.offer_price?.toDoubleOrNull() ?: 0.0
                            val cartQty = binding.cartQty.text.toString().toDoubleOrNull() ?: 0.0
                            TotalPrice = offerPrice * cartQty
                            binding.txtTotalPrice.text = "\u20b9$TotalPrice"

                            //cart check
                            for (j in response.body()?.ResponseCartList!!.indices) {
                                if (response.body()?.ResponseCartList!!.get(j).product_id.toInt() == (productsId.toInt())) {
                                    binding.cartQty.text=response.body()?.ResponseCartList!!.get(j).cart_quantity
                                    quantity[0]=response.body()?.ResponseCartList!!.get(j).cart_quantity.toInt()

                                    //cartId
                                    cartId = response.body()?.ResponseCartList!!.get(j).id


                                    //total amount showing
                                    val offerPrice = productResponseDetails?.offer_price?.toDoubleOrNull() ?: 0.0
                                    val cartQty = response.body()?.ResponseCartList!!.get(j).cart_quantity.toDoubleOrNull() ?: 0.0
                                    TotalPrice = offerPrice * cartQty
                                    binding.txtTotalPrice.text = "\u20b9"+offerPrice * cartQty
                                    //button status
                                    productCartStatus = "1";
                                    binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.gotocart)
                                }
                            }

                        } else {
                            binding.cartQty.text = "0"
                        }
                    } else {
                        ViewController.customToast(applicationContext, "Invalid Mobile Number")
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                binding.shimmerLoading.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })

    }

    //add to cart
    private fun addToCartApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.addToCartApi(
                getString(R.string.api_key),
                userId.toString(),
                productResponseDetails?.products_id.toString(),
                "1"
            )
        call.enqueue(object : Callback<AddtoCartResponse> {
            override fun onResponse(
                call: Call<AddtoCartResponse>,
                response: Response<AddtoCartResponse>
            ) {
                valueChangeDataSet("1")
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }

            override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                valueChangeDataSet("1")
            }
        })
    }

    //update Cart
    private fun upDateCartApi(cartQty: String) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.upDateCartApi(
                getString(R.string.api_key),
                userId.toString(),
                productResponseDetails?.products_id.toString(),
                cartQty
            )
        call.enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                valueChangeDataSet(cartQty)
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                valueChangeDataSet(cartQty)
            }
        })
    }

    //delete for cart
    private fun removeFromCartApi() {
        val userId = Preferences.loadStringValue(this@ProductsDetailsActivity, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.removeFromCartApi(
                getString(R.string.api_key),
                userId.toString(),
                productResponseDetails?.products_id.toString(),
                cartId            )
        call.enqueue(object : Callback<DeleteCartResponse> {
            override fun onResponse(
                call: Call<DeleteCartResponse>,
                response: Response<DeleteCartResponse>
            ) {
                valueChangeDataSet("0")
                try {
                    if(response.isSuccessful) {
                        val res = response.body()

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                valueChangeDataSet("0")
                Log.e("onFailure",t.message.toString())
            }
        })
    }

    private fun valueChangeDataSet(cartValue: String) {

        val offerPrice = productResponseDetails?.offer_price?.toDoubleOrNull() ?: 0.0
        val cartQty = cartValue.toDoubleOrNull() ?: 0.0
        TotalPrice = offerPrice * cartQty
        binding.txtTotalPrice.text = "\u20b9"+offerPrice * cartQty
        binding.cartQty.text = cartValue
        quantity = intArrayOf(cartValue.toInt())

        //button status
        if (cartValue == "0"){
            productCartStatus = "0";
            binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.addtocart)
        }else{
            productCartStatus = "1";
            binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.gotocart)
        }

    }

    override fun onResume() {
        super.onResume()
        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            productDetailsApi()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}