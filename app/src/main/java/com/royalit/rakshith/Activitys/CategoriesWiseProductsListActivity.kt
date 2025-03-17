package com.royalit.rakshith.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.rakshith.Activitys.AllProductsListActivity
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.CategoriesWiseProductsAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.CategoryWiseModel
import com.royalit.rakshith.Models.CategoryWiseResponse
import com.royalit.rakshith.Models.DeleteCartResponse
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.Models.UpdateCartResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCategoriesProductsListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesWiseProductsListActivity : AppCompatActivity(), CategoriesWiseProductsAdapter.ProductItemClick,
    CategoriesWiseProductsAdapter.CartItemQuantityChangeListener {

    val binding: ActivityCategoriesProductsListBinding by lazy {
        ActivityCategoriesProductsListBinding.inflate(layoutInflater)
    }


    lateinit var cartItemQuantityChangeListener: CategoriesWiseProductsAdapter.CartItemQuantityChangeListener
    lateinit var productItemClick: CategoriesWiseProductsAdapter.ProductItemClick

    //Products list
    var productList: List<CategoryWiseResponse> = ArrayList()
    var cartItemsList: List<CartItems> = ArrayList()

    var categoriesId: String = ""
    var categoryName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        categoriesId = intent.getStringExtra("categoriesId").toString()
        categoryName = intent.getStringExtra("categoryName").toString()

        inIts()

    }

    private fun inIts() {
        cartItemQuantityChangeListener = this@CategoriesWiseProductsListActivity
        productItemClick = this@CategoriesWiseProductsListActivity

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearCart.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            val intent = Intent(this@CategoriesWiseProductsListActivity, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(this@CategoriesWiseProductsListActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        binding.txtTitle.text = categoryName


        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCategoryWiseProductsListApi()
        }


    }

    //list
    private fun getCategoryWiseProductsListApi() {
        binding.shimmerLoading.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getCategoryWiseProductsListApi(
                getString(R.string.api_key),
                categoriesId,
            )
        call.enqueue(object : Callback<CategoryWiseModel> {
            override fun onResponse(
                call: Call<CategoryWiseModel>,
                response: Response<CategoryWiseModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        productList = response.body()?.response!!
                        if (productList.size > 0) {
                            binding.linearNoData.visibility = View.GONE
                            getCartApi()
                        } else {
                            binding.linearNoData.visibility = View.VISIBLE
                            binding.shimmerLoading.visibility = View.GONE
                        }
                    } else {
                        binding.linearNoData.visibility = View.GONE
                        binding.shimmerLoading.visibility = View.GONE
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    binding.linearNoData.visibility = View.GONE
                    binding.shimmerLoading.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<CategoryWiseModel>, t: Throwable) {
                binding.shimmerLoading.visibility = View.GONE
                binding.linearNoData.visibility = View.VISIBLE
                Log.e("onFailure",t.message.toString())
            }
        })

    }
    private fun getCartApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
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
                        updateCartCount(response.body()?.ResponseCartList!!.size.toString())
                        dataSet()
                    } else {
                        dataSet()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    dataSet()
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                binding.shimmerLoading.visibility = View.GONE
                dataSet()
            }
        })
    }
    private fun dataSet() {
        //empty list
        val cartListToPass = if (cartItemsList.isNullOrEmpty()) arrayListOf() else cartItemsList

        binding.recyclerview.layoutManager = LinearLayoutManager(this@CategoriesWiseProductsListActivity)
        binding.recyclerview.adapter = CategoriesWiseProductsAdapter(this@CategoriesWiseProductsListActivity, productList,cartListToPass, this@CategoriesWiseProductsListActivity, cartItemQuantityChangeListener)
    }

    override fun onProductItemClick(itemsData: CategoryWiseResponse) {

    }
    override fun onAddToCartClicked(itemsData: CategoryWiseResponse, cartQty: String, isAdd: Int) {
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

    private fun addToCart(itemsData: CategoryWiseResponse, cartQty: String) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.addToCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData.productsId.toString(),
                "1"
            )
        call.enqueue(object : Callback<AddtoCartResponse> {
            override fun onResponse(
                call: Call<AddtoCartResponse>,
                response: Response<AddtoCartResponse>
            ) {
                checkCartId(itemsData, "")
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
                checkCartId(itemsData, "")
            }
        })

    }

    private fun updateCart(itemsData: CategoryWiseResponse, cartQty: String) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.upDateCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData.productsId.toString(),
                cartQty,
            )
        call.enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                checkCartId(itemsData, "")
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
                Log.e("onFailure",t.message.toString())
                checkCartId(itemsData, "")
            }
        })

    }

    override fun onQuantityChanged(cartItem: CategoryWiseResponse, newQuantity: Int) {
//        val index = cartItemsList.indexOfFirst { it.product_id == cartItem.product_id }
//        if (index != -1) {
//            cartItemsList[index].cart_quantity = newQuantity.toString() // Now it's mutable
//            getTotalPrice(cartItemsList)
//        }
    }

    //delete item in cart
    override fun onDeleteCartItem(cartItem: CategoryWiseResponse) {
        checkCartId(cartItem, "1")
    }

    private fun checkCartId(cartItem: CategoryWiseResponse, deleteItem: String) {
        val userId = Preferences.loadStringValue(this@CategoriesWiseProductsListActivity, Preferences.userId, "")
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
                try {
                    if (response.isSuccessful) {

                        //Update cart count
                        updateCartCount(response.body()?.ResponseCartList!!.size.toString())

                        var cartItemsCheckId: List<CartItems> = ArrayList()
                        cartItemsCheckId = response.body()?.ResponseCartList!!
                        for (j in cartItemsCheckId.indices) {
                            if (cartItemsCheckId[j].product_id.toInt() == cartItem.productsId.toInt()) {
                                if (deleteItem.equals("1")){
                                    //CartId
                                    removeFromCartApi(cartItem, cartItemsCheckId[j].id.toString())
                                }
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun removeFromCartApi(cartItem: CategoryWiseResponse, cartID: String) {
        val userId = Preferences.loadStringValue(this@CategoriesWiseProductsListActivity, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.removeFromCartApi(
                getString(R.string.api_key),
                userId.toString(),
                cartItem.productsId,
                cartID
            )
        call.enqueue(object : Callback<DeleteCartResponse> {
            override fun onResponse(
                call: Call<DeleteCartResponse>,
                response: Response<DeleteCartResponse>
            ) {
                //Update cart count
                checkCartId(cartItem, "")
                try {
                    if(response.isSuccessful) {

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                //Update cart count
                checkCartId(cartItem, "")
            }
        })
    }

    private fun updateCartCount(count: String) {
        binding.root.findViewById<TextView>(R.id.cart_badge_count).text = count
        Preferences.saveStringValue(this@CategoriesWiseProductsListActivity, Preferences.cartCount, count)
    }

    override fun onResume() {
        super.onResume()
        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCategoryWiseProductsListApi()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}