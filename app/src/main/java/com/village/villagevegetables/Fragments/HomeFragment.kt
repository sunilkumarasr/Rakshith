package com.village.villagevegetables.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.village.villagevegetables.Activitys.AllProductsListActivity
import com.village.villagevegetables.Activitys.CategoriesActivity
import com.village.villagevegetables.Activitys.CategoriesWiseProductsListActivity
import com.village.villagevegetables.Activitys.DashBoardActivity
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Adapters.HomeBannersAdapter
import com.village.villagevegetables.Adapters.HomeCategoriesAdapter
import com.village.villagevegetables.Adapters.HomeFeatureProductsAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddtoCartResponse
import com.village.villagevegetables.Models.BannersModel
import com.village.villagevegetables.Models.BannersResponse
import com.village.villagevegetables.Models.CategoryListResponse
import com.village.villagevegetables.Models.CategoryModel
import com.village.villagevegetables.Models.DeleteCartResponse
import com.village.villagevegetables.Models.ProductListResponse
import com.village.villagevegetables.Models.ProductModel
import com.village.villagevegetables.Models.UpdateCartResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.village.villagevegetables.Logins.LoginActivity


class HomeFragment : Fragment() , HomeFeatureProductsAdapter.ProductItemClick,
    HomeFeatureProductsAdapter.CartItemQuantityChangeListener {

    private lateinit var binding: FragmentHomeBinding

    lateinit var cartItemQuantityChangeListener: HomeFeatureProductsAdapter.CartItemQuantityChangeListener
    lateinit var productItemClick: HomeFeatureProductsAdapter.ProductItemClick

    //Products list
    var productList: MutableList<ProductListResponse> = ArrayList()
    var cartItemsList: List<CartItems> = ArrayList()

    //banners
    private lateinit var handler : Handler
    private lateinit var adapter: HomeBannersAdapter

    var userId: String = "";

    var cityId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        cartItemQuantityChangeListener = this
        productItemClick = this

        handler = Handler(Looper.myLooper()!!)

        userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "").toString()
        cityId = Preferences.loadStringValue(requireActivity(), Preferences.cityId, "").toString()


        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {
            getBannersApi()
            getCategoriesApi()
            getProductsApi()
        }

        //banners
        setUpTransformer()
        binding.viewPagerBanners.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 4000)
            }
        })

        binding.viewMoreProducts.setOnClickListener {
            val animations = ViewController.animation()
            binding.viewMoreProducts.startAnimation(animations)
            val intent = Intent(activity, AllProductsListActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.viewMoreCategories.setOnClickListener {
            val animations = ViewController.animation()
            binding.viewMoreCategories.startAnimation(animations)
//            val intent = Intent(activity, CategoriesActivity::class.java)
//            startActivity(intent)
//            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)

            val intent = Intent(activity, CategoriesWiseProductsListActivity::class.java).apply {
                putExtra("categoriesId", "")
                putExtra("categoryName", "")
            }
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)

        }

        binding.cardViewOrder.setOnClickListener {
            val phoneIntent = Intent(Intent.ACTION_DIAL) // or Intent.ACTION_CALL if you have permission
            phoneIntent.data = Uri.parse("tel:9441085061")
            startActivity(phoneIntent)
        }

    }

    private fun getBannersApi() {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getBannersApi(getString(R.string.api_key))
        call.enqueue(object : Callback<BannersModel> {
            override fun onResponse(call: Call<BannersModel>, response: Response<BannersModel>) {
                try {
                    if (response.isSuccessful) {
                        val bannersServicesList = response.body()?.response
                        //empty
                        if (bannersServicesList.isNullOrEmpty()) {
                            binding.viewPagerBanners.visibility = View.GONE
                            return
                        }
                        dataSetBanners(bannersServicesList)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }
            override fun onFailure(call: Call<BannersModel>, t: Throwable) {
                Log.e("onFailurebannersServicesList", "API Call Failed: ${t.message}")
            }
        })
    }
    private fun dataSetBanners(bannersselectedServicesList: ArrayList<BannersResponse>) {
        adapter = HomeBannersAdapter(requireActivity(), bannersselectedServicesList, binding.viewPagerBanners)

        binding.viewPagerBanners.adapter = adapter
        binding.viewPagerBanners.offscreenPageLimit = 3
        binding.viewPagerBanners.clipToPadding = false
        binding.viewPagerBanners.clipChildren = false
        binding.viewPagerBanners.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
    private val runnable = Runnable {
        binding.viewPagerBanners.currentItem = binding.viewPagerBanners.currentItem + 1
    }
    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(1))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.10f
//            page.alpha = 0.50f + (1 - abs(position)) * 0.50f
        }
        binding.viewPagerBanners.setPageTransformer(transformer)
    }


    private fun getCategoriesApi() {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCategoriesApi(getString(R.string.api_key))
        call.enqueue(object : Callback<CategoryModel> {
            override fun onResponse(call: Call<CategoryModel>, response: Response<CategoryModel>) {
                try {
                    if (response.isSuccessful) {
                        val selectedServicesList = response.body()?.response
                        //empty
                        if (selectedServicesList.isNullOrEmpty()) {
                            binding.linearCategoriesHeader.visibility = View.GONE
                            binding.recyclerViewCategories.visibility = View.GONE
                            return
                        }
                        dataSet(selectedServicesList)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }
            override fun onFailure(call: Call<CategoryModel>, t: Throwable) {
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }
    private fun dataSet(selectedServicesList: List<CategoryListResponse>) {
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCategories.adapter  = HomeCategoriesAdapter(requireActivity(), selectedServicesList) { item ->
                val intent = Intent(activity, CategoriesWiseProductsListActivity::class.java).apply {
                    putExtra("categoriesId", item.categoriesId)
                    putExtra("categoryName", item.categoryName)
                }
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
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
                        if (productList.isNotEmpty()){
                            getCartApi()
                        }else{
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
                binding.recyclerViewProducts.visibility = View.GONE
                binding.shimmerLoading.visibility = View.GONE

            }
        })
    }
    private fun getCartApi() {

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

                        Preferences.saveStringValue(requireActivity(), Preferences.cartCount, cartItemsList.size.toString())

                        //cart count
                        val activity = activity as DashBoardActivity
                        activity.cartCount(cartItemsList.size.toString(), "")

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

        binding.recyclerViewProducts.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerViewProducts.adapter = HomeFeatureProductsAdapter(requireActivity(), productList,cartListToPass, this, cartItemQuantityChangeListener)

    }

    override fun onProductItemClick(itemsData: ProductListResponse) {

    }

    override fun onAddToCartClicked(itemsData: ProductListResponse, cartQty: String, isAdd: Int) {
        if (isAdd == 0) {
            if (!ViewController.noInterNetConnectivity(requireActivity())) {
                ViewController.showToast(requireActivity(), "Please check your connection ")
            } else {
                addToCart(itemsData, cartQty)
            }
        }else{
            if (!ViewController.noInterNetConnectivity(requireActivity())) {
                ViewController.showToast(requireActivity(), "Please check your connection ")
            } else {
                updateCart(itemsData, cartQty)
            }
        }
    }

    private fun addToCart(itemsData: ProductListResponse, cartQty: String) {
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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                    checkCartId(itemsData, "")
                }
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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                    checkCartId(itemsData, "")
                }
            }
        })

    }

    private fun updateCart(itemsData: ProductListResponse, cartQty: String) {
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.upDateCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData.productsId.toString(),
                cartQty.toString(),
            )
        call.enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                    checkCartId(itemsData, "")
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()

                    }
                }catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                    checkCartId(itemsData, "")
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
        checkCartId(cartItem, "1")
    }
    private fun checkCartId(cartItem: ProductListResponse, deleteItem: String) {
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
                        (cartItemsCheckId as? MutableList<CartItems>)?.clear()

                        cartItemsCheckId = response.body()?.ResponseCartList!!
                        for (j in cartItemsCheckId.indices) {
                            if (cartItemsCheckId[j].product_id.toInt() == cartItem.productsId.toInt()) {

                                //Update cart count
                                updateCartCount(response.body()?.ResponseCartList!!.size.toString(), "")

                                if (deleteItem == "1"){
                                    //CartId
                                    removeFromCartApi(cartItem, cartItemsCheckId[j].id.toString(), response.body()?.ResponseCartList!!.size.toString())
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
    private fun removeFromCartApi(cartItem: ProductListResponse, cartID: String, count: String) {
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
                updateCartCount(count, "1")
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
                updateCartCount(count, "1")
            }
        })
    }

    private fun updateCartCount(count: String, cartStatus: String) {
        val activity = activity as DashBoardActivity
        activity.cartCount(count, cartStatus)
    }

    override fun onResume() {
        super.onResume()
        getProductsApi()

        handler.postDelayed(runnable , 2000)
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }


}