package com.royalit.rakshith.Activitys

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.ProductDetailsModel
import com.royalit.rakshith.Adapters.ProductDetailsResponse
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.UpdateCartResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityProductsDetailsBinding
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
    var TotalPrice: Double? = 0.0
    private var TotalFinalPrice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

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
                binding.imgFav.setImageResource(R.drawable.favadd)
            } else {
                binding.imgFav.setImageResource(R.drawable.favorite_ic)
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
                if ((productResponseDetails?.max_order_quantity!=null)&& (productResponseDetails?.max_order_quantity!!.toInt()<=cartQty.toInt())){
                    ViewController.customToast(this@ProductsDetailsActivity,"Can't add Max Quantity for this Product" + productResponseDetails?.max_order_quantity)
                    return@setOnClickListener
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

        }
        binding.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearDecrement.startAnimation(animations)
            if (quantity[0] > 1) {
                quantity[0]--
                binding.cartQty.text = quantity[0].toString()
                val cartQty1 = binding.cartQty.text.toString()
                upDateCartApi(cartQty1)
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
            } else {
                if ((productResponseDetails?.max_order_quantity!=null)&& (productResponseDetails?.max_order_quantity!!.toInt()<=cartQty1.toInt())){
                    ViewController.customToast(this@ProductsDetailsActivity,"Max Quantity only for "+productResponseDetails?.max_order_quantity)
                    return@setOnClickListener
                }
                try {
                    quantity[0]++
                    binding.cartQty.text = "" + quantity[0]
                    val cQty = binding.cartQty.text.toString()
                    if (cQty == "1"){
                        addToCartApi()
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            productDetailsApi()
            getCartApi()
        }

    }

    //details api
    private fun productDetailsApi() {
        binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        productResponseDetails = response.body()?.response!!
                        DataSet(productResponseDetails!!)
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<ProductDetailsModel>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun DataSet(productResponse: ProductDetailsResponse) {

        //out of stock
        if (productResponse.stock.toInt() ==0) {
            binding.linearOutOfStock.visibility = View.VISIBLE
            binding.linearInStock.visibility = View.GONE
            binding.linearBottom.visibility = View.GONE
        } else {
            binding.linearOutOfStock.visibility = View.GONE
            binding.linearInStock.visibility = View.VISIBLE
            binding.linearBottom.visibility = View.VISIBLE
        }

        Glide.with(binding.imgProduct)
            .load(productResponse.product_image)
            .error(R.drawable.logo)
            .into(binding.imgProduct)
        binding.txtTitle.text = productResponse.product_title
        binding.txtOfferPrice.text = "Price: "+ "\u20B9 " + productResponse.offer_price
        binding.txtActualPrice.text = "Price: "+ "\u20B9 " + productResponse.sales_price
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
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.ResponseCartList?.size!! > 0) {
                            //total amount showing
                            getTotalPrice(response.body()?.ResponseCartList!!)
                            binding.cartQty.text = "0"
                            for (j in response.body()?.ResponseCartList!!.indices) {
                                if (response.body()?.ResponseCartList!!.get(j).product_id.toInt() == (productsId.toInt())) {
                                    binding.cartQty.text=response.body()?.ResponseCartList!!.get(j).cart_quantity
                                    quantity[0]=response.body()?.ResponseCartList!!.get(j).cart_quantity.toInt()
                                    if (!response.body()?.ResponseCartList!!.get(j).cart_quantity.equals("0")){
                                        productCartStatus = "1";
                                        binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.gotocart)
                                    }else{
                                        productCartStatus = "0";
                                        binding.txtAddToCart.text = this@ProductsDetailsActivity.getString(R.string.addtocart)
                                    }
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
                Log.e("onFailure",t.message.toString())
            }
        })

    }

    //add to cart
    private fun addToCartApi() {
        binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.customToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.message.equals("Success")){

                            }else{
                                ViewController.customToast(this@ProductsDetailsActivity,res.message)
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                binding.progressBar.visibility = View.GONE
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.customToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
            }
        })
    }

    //update Cart
    private fun upDateCartApi(cartQty: String) {
        binding.progressBar.visibility = View.VISIBLE
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
                binding.progressBar.visibility = View.GONE
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.customToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            getCartApi()
                            if (res.message.equals("Success")){

                            }else{
                                ViewController.customToast(this@ProductsDetailsActivity,res.message)
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                binding.progressBar.visibility = View.GONE
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.customToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
            }
        })
    }

    //total amount
    @SuppressLint("SetTextI18n")
    private fun getTotalPrice(cartItemsList: List<CartItems>) {
        try {
            TotalPrice = 0.0
            for (i in cartItemsList.indices) {
                try {
                    TotalPrice = TotalPrice!! + cartItemsList[i].offer_price
                        .toDouble() * cartItemsList[i].cart_quantity.toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.txtTotalPrice.text = "\u20b9 $TotalPrice"
            TotalFinalPrice = TotalPrice.toString()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}