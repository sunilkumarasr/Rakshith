package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.village.villagevegetables.Adapters.CategoriesAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.CategoryListResponse
import com.village.villagevegetables.Models.CategoryModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityCategoriesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesActivity : AppCompatActivity() {

    val binding: ActivityCategoriesBinding by lazy {
        ActivityCategoriesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }


        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(this@CategoriesActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCategoriesApi()
        }

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
                        if (selectedServicesList != null) {
                            if (!selectedServicesList.isEmpty()) {
                                DataSet(selectedServicesList)
                            }else{
                                binding.linearNoData.visibility = View.VISIBLE
                            }
                        }else{
                            binding.linearNoData.visibility = View.VISIBLE
                        }

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
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(this@CategoriesActivity, 3)
            binding.recyclerview.adapter  = CategoriesAdapter(selectedServicesList) { item ->
                val intent = Intent(this@CategoriesActivity, CategoriesWiseProductsListActivity::class.java).apply {
                    putExtra("categoriesId", item.categoriesId)
                    putExtra("categoryName", item.categoryName)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }
    }


}