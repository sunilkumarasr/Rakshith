package com.royalit.rakshith.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.royalit.rakshith.Activitys.AllProductsListActivity
import com.royalit.rakshith.Activitys.CategoriesActivity
import com.royalit.rakshith.Activitys.CategoriesWiseProductsListActivity
import com.royalit.rakshith.Activitys.SearchActivity
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.HomeCategoriesAdapter
import com.royalit.rakshith.Adapters.HomeFeatureProductsAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.CategoryListResponse
import com.royalit.rakshith.Models.CategoryModel
import com.royalit.rakshith.Models.DeleteCartResponse
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.Models.ProductModel
import com.royalit.rakshith.Models.UpdateCartResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() , HomeFeatureProductsAdapter.ProductItemClick,
    HomeFeatureProductsAdapter.CartItemQuantityChangeListener {

    private lateinit var binding: FragmentHomeBinding

    lateinit var cartItemQuantityChangeListener: HomeFeatureProductsAdapter.CartItemQuantityChangeListener
    lateinit var productItemClick: HomeFeatureProductsAdapter.ProductItemClick


    //Products list
    var productList: MutableList<ProductListResponse> = ArrayList()
    var cartItemsList: List<CartItems> = ArrayList()

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

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {
            HomebannersApi()
            getCategoriesApi()
            getProductsApi()
        }

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
            val intent = Intent(activity, CategoriesActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

    }

    private fun HomebannersApi() {
        val imageList = mutableListOf<SlideModel>()
        imageList.add(SlideModel(R.drawable.dummy_banner1, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner1, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        binding.imageSlider.setImageList(imageList)
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
                        DataSet(selectedServicesList)
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
    private fun DataSet(selectedServicesList: List<CategoryListResponse>) {
        // Truncate the list to the first 6 items if needed
       // val truncatedList = selectedServicesList.take(6)
        binding.recyclerViewCategories.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewCategories.adapter  = HomeCategoriesAdapter(selectedServicesList) { item ->
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
                binding.recyclerViewProducts.visibility = View.GONE
                binding.shimmerLoading.visibility = View.GONE

            }
        })
    }
    private fun getCartApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
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

        binding.recyclerViewProducts.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerViewProducts.adapter = HomeFeatureProductsAdapter(requireActivity(), productList,cartListToPass, this, cartItemQuantityChangeListener)

    }

    override fun onProductItemClick(itemsData: ProductListResponse?) {

    }
    override fun onAddToCartClicked(itemsData: ProductListResponse?, cartQty: String?, isAdd: Int) {
        if (isAdd == 0) {
            if (!ViewController.noInterNetConnectivity(requireActivity())) {
                ViewController.showToast(requireActivity(), "Please check your connection ")
            } else {
                addToCart(itemsData, cartQty)
            }
        } else{
            if (!ViewController.noInterNetConnectivity(requireActivity())) {
                ViewController.showToast(requireActivity(), "Please check your connection ")
            } else {
                updateCart(itemsData, cartQty)
            }
        }
    }

    private fun addToCart(itemsData: ProductListResponse?, cartQty: String?) {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                   // getProductsApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.message.equals("Success")){

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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                   // getProductsApi()
                }
            }
        })

    }

    private fun updateCart(itemsData: ProductListResponse?, cartQty: String?) {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                    //getProductsApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()
                        if (res != null) {
                            if (res.message.equals("Success")){

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
                if (!ViewController.noInterNetConnectivity(requireActivity())) {
                    ViewController.showToast(requireActivity(), "Please check your connection ")
                } else {
                   // getProductsApi()
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
        removeFromCartApi(cartItem)
    }
    private fun removeFromCartApi(cartItem: ProductListResponse) {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.removeFromCartApi(
                getString(R.string.api_key),
                userId.toString(),
                cartItem.productsId,
                cartItem.cartId
            )
        call.enqueue(object : Callback<DeleteCartResponse> {
            override fun onResponse(
                call: Call<DeleteCartResponse>,
                response: Response<DeleteCartResponse>
            ) {
                try {
                    if(response.isSuccessful) {

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                getCartApi()
                Log.e("onFailure",t.message.toString())
            }
        })
    }


    override fun onResume() {
        super.onResume()
        getProductsApi()
    }

}