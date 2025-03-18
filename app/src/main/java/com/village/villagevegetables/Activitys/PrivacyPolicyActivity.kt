package com.village.villagevegetables.Activitys

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.PrivacyPolicyModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityPrivacyPolicyBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrivacyPolicyActivity : AppCompatActivity() {

    val binding: ActivityPrivacyPolicyBinding by lazy {
        ActivityPrivacyPolicyBinding.inflate(layoutInflater)
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



        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            privacyPolicyApi()
        }


    }

    private fun privacyPolicyApi() {
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.privacyPolicyApi(
                getString(R.string.api_key)
            )
        call.enqueue(object : Callback<PrivacyPolicyModel> {
            override fun onResponse(
                call: Call<PrivacyPolicyModel>,
                response: Response<PrivacyPolicyModel>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.privcypolicyResponse?.let { listOfcategories ->
                            if (listOfcategories.isNotEmpty()) {
                                val htmlText = listOfcategories[0].privacy
                                binding.txtNote.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
                            }
                        } ?: run {
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<PrivacyPolicyModel>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}