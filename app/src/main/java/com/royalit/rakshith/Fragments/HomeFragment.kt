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
import com.royalit.rakshith.Activitys.CategoriesWiseProductsListActivity
import com.royalit.rakshith.Activitys.ProductsDetailsActivity
import com.royalit.rakshith.Activitys.SearchActivity
import com.royalit.rakshith.Adapters.AllProductsAdapter
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.HomeCategoriesAdapter
import com.royalit.rakshith.Adapters.HomeFeatureProductsAdapter
import com.royalit.rakshith.Adapters.Search.SearchAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddtoCartResponse
import com.royalit.rakshith.Models.CategoryListResponse
import com.royalit.rakshith.Models.CategoryModel
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.Models.ProductModel
import com.royalit.rakshith.Models.UpdateCartResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //Products list
    var productList: List<ProductListResponse> = ArrayList()
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

        binding.linearSearch.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSearch.startAnimation(animations)
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }


    }

    private fun HomebannersApi() {
        val imageList = mutableListOf<SlideModel>()
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
        imageList.add(SlideModel(R.drawable.dummy_banner, ""))
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
                            binding.txtCatHeader.visibility = View.GONE
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
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getProductsApi(getString(R.string.api_key))
        call.enqueue(object : Callback<ProductModel> {
            override fun onResponse(call: Call<ProductModel>, response: Response<ProductModel>) {
                try {
                    if (response.isSuccessful) {
                        productList = response.body()?.response!!
                        DataProductSet()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }

            override fun onFailure(call: Call<ProductModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                binding.recyclerViewProducts.visibility = View.GONE
            }
        })
    }
    private fun DataProductSet() {
        binding.recyclerViewProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewProducts.adapter = HomeFeatureProductsAdapter(productList) { item ->
            val intent = Intent(requireActivity(), ProductsDetailsActivity::class.java).apply {
                putExtra("productsId", item.productsId)
            }
            startActivity(intent)
        }
    }

}