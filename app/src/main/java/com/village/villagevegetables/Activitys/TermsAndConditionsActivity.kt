package com.village.villagevegetables.Activitys

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.TermsAndConditionsModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityTermsAndConditionsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsAndConditionsActivity : AppCompatActivity() {

    val binding: ActivityTermsAndConditionsBinding by lazy {
        ActivityTermsAndConditionsBinding.inflate(layoutInflater)
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

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            //termsAndConditionsApi()
        }



        binding.webView.settings.javaScriptEnabled = true
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(RetrofitClient.URL+"Terms_conditions.php")



    }

    private fun termsAndConditionsApi() {
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.termsAndConditionsApi(
                getString(R.string.api_key)
            )
        call.enqueue(object : Callback<TermsAndConditionsModel> {
            override fun onResponse(
                call: Call<TermsAndConditionsModel>,
                response: Response<TermsAndConditionsModel>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.termsandconditionsResponse?.let { listOfcategories ->
                            if (listOfcategories.isNotEmpty()) {
                                val htmlText = listOfcategories[0].terms
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
            override fun onFailure(call: Call<TermsAndConditionsModel>, t: Throwable) {
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