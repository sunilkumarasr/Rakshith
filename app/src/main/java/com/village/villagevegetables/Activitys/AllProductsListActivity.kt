package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.village.villagevegetables.Adapters.AllProductsAdapter
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddtoCartResponse
import com.village.villagevegetables.Models.CategoryWiseResponse
import com.village.villagevegetables.Models.DeleteCartResponse
import com.village.villagevegetables.Models.ProductListResponse
import com.village.villagevegetables.Models.ProductModel
import com.village.villagevegetables.Models.UpdateCartResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityAllProductsListBinding
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


    var cityId : String = ""

    //Products list
    var productList: MutableList<ProductListResponse> = ArrayList()
    var cartItemsList: List<CartItems> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        cartItemQuantityChangeListener = this@AllProductsListActivity
        productItemClick = this@AllProductsListActivity

        cityId = Preferences.loadStringValue(this@AllProductsListActivity, Preferences.cityId, "").toString()


        val cartCount = Preferences.loadStringValue(applicationContext, Preferences.cartCount, "")
        binding.root.findViewById<TextView>(R.id.cart_badge_count).text = cartCount.toString()

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

                        val responseList = response.body()?.response
                        productList = responseList?.filter {
                            !it.locationIds.isNullOrEmpty() && it.locationIds.contains(cityId)
                        }?.toMutableList() ?: mutableListOf()

                        if (productList.size > 0) {
                            binding.linearNoData.visibility = View.GONE
                            getCartApi()
                        } else {
                            binding.linearNoData.visibility = View.VISIBLE
                            binding.shimmerLoading.visibility = View.GONE
                        }

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
                        updateCartCount(response.body()?.ResponseCartList!!.size.toString())
                        dataProductSet()
                    } else {
                        dataProductSet()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    dataProductSet()
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                dataProductSet()
                binding.shimmerLoading.visibility = View.GONE
            }
        })
    }
    private fun dataProductSet() {
        //empty list
        val cartListToPass = if (cartItemsList.isNullOrEmpty()) arrayListOf() else cartItemsList

        binding.recyclerview.layoutManager = GridLayoutManager(this@AllProductsListActivity, 2)
        binding.recyclerview.adapter = AllProductsAdapter(this@AllProductsListActivity, productList,cartListToPass, this@AllProductsListActivity, cartItemQuantityChangeListener)
    }

    override fun onProductItemClick(itemsData: ProductListResponse) {

    }
    override fun onAddToCartClicked(itemsData: ProductListResponse, cartQty: String, isAdd: Int) {
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

    private fun addToCart(itemsData: ProductListResponse, cartQty: String) {
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
                getProductsApiwithoutAnimation()
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
                getProductsApiwithoutAnimation()
            }
        })

    }

    private fun updateCart(itemsData: ProductListResponse, cartQty: String) {
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
                getProductsApiwithoutAnimation()
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
                getProductsApiwithoutAnimation()
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
        checkCartId(cartItem, "1")
    }
    private fun checkCartId(cartItem: ProductListResponse, deleteItem: String) {
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
                try {
                    if (response.isSuccessful) {

                        var cartItemsCheckId: List<CartItems> = ArrayList()
                        // To clear the list
                        (cartItemsCheckId as MutableList<CartItems>).clear()
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
    private fun removeFromCartApi(cartItem: ProductListResponse, cartID: String) {
        val userId = Preferences.loadStringValue(this@AllProductsListActivity, Preferences.userId, "")
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
                getProductsApiwithoutAnimation()
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
                Log.e("onFailure",t.message.toString())
                //Update cart count
                getProductsApiwithoutAnimation()
            }
        })
    }

    private fun updateCartCount(count: String) {
        binding.root.findViewById<TextView>(R.id.cart_badge_count).text = count
        Preferences.saveStringValue(this@AllProductsListActivity, Preferences.cartCount, count)
    }

    //reload api
    private fun getProductsApiwithoutAnimation() {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getProductsApi(getString(R.string.api_key))
        call.enqueue(object : Callback<ProductModel> {
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                try {
                    if (response.isSuccessful) {
                        productList = response.body()?.response!!
                        getCartApiwithoutAnimation()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }

            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
            }
        })
    }
    private fun getCartApiwithoutAnimation() {
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
                try {
                    if (response.isSuccessful) {
                        cartItemsList = response.body()?.ResponseCartList!!
                        updateCartCount(response.body()?.ResponseCartList!!.size.toString())
                        dataProductSetwithoutAnimation()
                    } else {
                        dataProductSetwithoutAnimation()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    dataProductSetwithoutAnimation()
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                dataProductSetwithoutAnimation()
            }
        })
    }
    private fun dataProductSetwithoutAnimation() {
        val layoutManager = binding.recyclerview.layoutManager as GridLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemOffset = layoutManager.findViewByPosition(firstVisibleItemPosition)?.top ?: 0

        val cartListToPass = if (cartItemsList.isNullOrEmpty()) arrayListOf() else cartItemsList

        if (binding.recyclerview.layoutManager == null) {
            binding.recyclerview.layoutManager = GridLayoutManager(this@AllProductsListActivity, 2)
        }

        binding.recyclerview.setHasFixedSize(true)

        val adapter = binding.recyclerview.adapter as? AllProductsAdapter
        adapter?.updateData(productList, cartListToPass)

        binding.recyclerview.post {
            layoutManager.scrollToPositionWithOffset(firstVisibleItemPosition, visibleItemOffset)
        }
    }


}