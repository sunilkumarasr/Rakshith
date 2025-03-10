package com.royalit.rakshith.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.royalit.rakshith.Adapters.AllProductsAdapter
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.CategoriesWiseProductsAdapter
import com.royalit.rakshith.Adapters.HomeFeatureProductsAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.CategoryWiseResponse
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.Models.ProductModel
import com.royalit.rakshith.Models.UpdateCartResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityAllProductsListBinding
import com.royalit.rakshith.databinding.ActivityCategoriesProductsListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllProductsListActivity : AppCompatActivity() , AllProductsAdapter.ProductItemClick,
    AllProductsAdapter.CartItemQuantityChangeListener {

    val binding: ActivityAllProductsListBinding by lazy {
        ActivityAllProductsListBinding.inflate(layoutInflater)
    }

    lateinit var cartItemQuantityChangeListener: AllProductsAdapter.CartItemQuantityChangeListener
    lateinit var productItemClick: AllProductsAdapter.ProductItemClick


    //Products list
    var productList: List<ProductListResponse> = ArrayList()
    var cartItemsList: List<CartItems> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        cartItemQuantityChangeListener = this@AllProductsListActivity
        productItemClick = this@AllProductsListActivity

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearCart.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            val intent = Intent(this@AllProductsListActivity, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(this@AllProductsListActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getProductsApi()
        }

    }

    private fun getProductsApi() {
        binding.shimmerLoading.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getProductsApi(getString(R.string.api_key))
        call.enqueue(object : Callback<ProductModel> {
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                try {
                    if (response.isSuccessful) {
                        productList = response.body()?.response!!
                        getCartApi()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.shimmerLoading.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                binding.recyclerview.visibility = View.GONE
                binding.shimmerLoading.visibility = View.GONE
            }
        })
    }
    private fun getCartApi() {
        val userId = Preferences.loadStringValue(this@AllProductsListActivity, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCartApi(
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
                        cartItemsList = response.body()?.ResponseCartList!!
                        DataProductSet()
                    } else {
                        DataProductSet()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    DataProductSet()
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                DataProductSet()
                binding.shimmerLoading.visibility = View.GONE
            }
        })
    }
    private fun DataProductSet() {
        //empty list
        val cartListToPass = if (cartItemsList.isNullOrEmpty()) arrayListOf() else cartItemsList

        binding.recyclerview.layoutManager = GridLayoutManager(this@AllProductsListActivity, 2)
        binding.recyclerview.adapter = AllProductsAdapter(this@AllProductsListActivity, productList,cartListToPass, this@AllProductsListActivity, cartItemQuantityChangeListener)

    }

    override fun onProductItemClick(itemsData: ProductListResponse?) {

    }
    override fun onAddToCartClicked(itemsData: ProductListResponse?, cartQty: String?, isAdd: Int) {
        if (isAdd == 0) {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                addToCart(itemsData, cartQty)
            }
        } else{
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                updateCart(itemsData, cartQty)
            }
        }
    }

    private fun addToCart(itemsData: ProductListResponse?, cartQty: String?) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.addToCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData?.productsId.toString(),
                "1"
            )
        call.enqueue(object : Callback<AddtoCartResponse> {
            override fun onResponse(
                call: Call<AddtoCartResponse>,
                response: Response<AddtoCartResponse>
            ) {
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
//                    getProductsApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.message.equals("Success")){

                            }else{
                                ViewController.showToast(this@AllProductsListActivity,res.message)
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
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
//                    getProductsApi()
                }
            }
        })

    }

    private fun updateCart(itemsData: ProductListResponse?, cartQty: String?) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.upDateCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData?.productsId.toString(),
                cartQty!!,
            )
        call.enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
//                    getProductsApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.message.equals("Success")){

                            }else{
                                ViewController.showToast(this@AllProductsListActivity,res.message)
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
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
//                    getProductsApi()
                }
            }
        })

    }

    override fun onQuantityChanged(cartItem: ProductListResponse, newQuantity: Int) {
//        val index = cartItemsList.indexOfFirst { it.product_id == cartItem.product_id }
//        if (index != -1) {
//            cartItemsList[index].cart_quantity = newQuantity.toString() // Now it's mutable
//            getTotalPrice(cartItemsList)
//        }
    }

    //delete item in cart
    override fun onDeleteCartItem(cartItem: ProductListResponse) {

    }


}