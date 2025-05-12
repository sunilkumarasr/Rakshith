package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.village.villagevegetables.Adapters.Search.SearchAdapter
import com.village.villagevegetables.Adapters.Search.SearchItems
import com.village.villagevegetables.Adapters.Search.SearchModel
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.ProductListResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {


    val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    //Products list
    var productList: MutableList<SearchItems> = ArrayList()

    var cityId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {

        cityId = Preferences.loadStringValue(this@SearchActivity, Preferences.cityId, "").toString()


        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }


        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.toString().length > 1) {
                    binding.progressBar.visibility = View.VISIBLE

                    if (!ViewController.noInterNetConnectivity(applicationContext)) {
                        ViewController.showToast(applicationContext, "Please check your connection ")
                    } else {
                        searchApi(s.toString())
                    }

                }
            }


        })


    }

    private fun searchApi(resultSearch: String) {
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.searchApi(
                getString(R.string.api_key),
                resultSearch,
            )
        call.enqueue(object : Callback<SearchModel> {
            override fun onResponse(
                call: Call<SearchModel>,
                response: Response<SearchModel>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        val responseList = response.body()?.responsecartList!!
                        productList = responseList?.filter {
                            !it.locationIds.isNullOrEmpty() && it.locationIds.contains(cityId)
                        }?.toMutableList() ?: mutableListOf()


                        if (productList.isNotEmpty()){
                            binding.txtNoData.visibility = View.GONE
                            binding.recyclerview.visibility = View.VISIBLE
                            DataSet(productList)
                        }else{
                            binding.txtNoData.visibility = View.VISIBLE
                            binding.recyclerview.visibility = View.GONE
                        }

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<SearchModel>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun DataSet(categories: List<SearchItems>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity)
        binding.recyclerview.adapter = SearchAdapter(categories) { item ->
            val intent = Intent(this@SearchActivity, ProductsDetailsActivity::class.java).apply {
                putExtra("productsId", item.products_id)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }


}