package com.village.villagevegetables.Activitys

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.village.villagevegetables.Adapters.FaqAdapter
import com.village.villagevegetables.Adapters.FaqModel
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityFaqBinding

class FaqActivity : AppCompatActivity() {


    val binding: ActivityFaqBinding by lazy {
        ActivityFaqBinding.inflate(layoutInflater)
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



        if(!ViewController.noInterNetConnectivity(this@FaqActivity)){
            ViewController.showToast(this@FaqActivity, "Please check your connection ")
            return
        }else {
            faqListApi()
        }

    }

    private fun faqListApi() {

        ViewController.showLoading(this@FaqActivity)
        val apiInterface = RetrofitClient.apiInterface
        apiInterface.faqListApi().enqueue(object : retrofit2.Callback<List<FaqModel>> {
            override fun onResponse(
                call: retrofit2.Call<List<FaqModel>>,
                response: retrofit2.Response<List<FaqModel>>
            ) {
                ViewController.hideLoading()
                if (response.isSuccessful) {
                    val rsp = response.body()
                    if (rsp != null) {
                        val FaqList = response.body()
                        if (FaqList != null) {
                            FaqDataSet(FaqList)
                        }
                    } else {

                    }
                } else {
                    ViewController.showToast(
                        this@FaqActivity,
                        "Error: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: retrofit2.Call<List<FaqModel>>, t: Throwable) {
                Log.e("cat_error", t.message.toString())
                ViewController.hideLoading()
                ViewController.showToast(this@FaqActivity, "Try again: ${t.message}")
            }
        })

    }
    private fun FaqDataSet(faqlist: List<FaqModel>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@FaqActivity)
        binding.recyclerview.adapter = FaqAdapter(faqlist) { item ->

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}