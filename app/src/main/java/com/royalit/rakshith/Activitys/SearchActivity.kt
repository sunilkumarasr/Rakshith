package com.royalit.rakshith.Activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.Search.SearchAdapter
import com.royalit.rakshith.Adapters.Search.SearchItems
import com.royalit.rakshith.Adapters.Search.SearchModel
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.LoginModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCartBinding
import com.royalit.rakshith.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {


    val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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
                        val searchList = response.body()?.responsecartList!!
                        if (searchList.isEmpty()) {
                            binding.txtNoData.visibility = View.VISIBLE
                            binding.recyclerview.visibility = View.GONE
                        }else{
                            binding.txtNoData.visibility = View.GONE
                            binding.recyclerview.visibility = View.VISIBLE
                            DataSet(searchList)
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