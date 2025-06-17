package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Logins.LoginActivity
import com.village.villagevegetables.Models.AboutUsModel
import com.village.villagevegetables.Models.DeleteModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityAboutUsBinding
import com.village.villagevegetables.databinding.ActivityDeleteAccountBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountActivity : AppCompatActivity() {

    val binding: ActivityDeleteAccountBinding by lazy {
        ActivityDeleteAccountBinding.inflate(layoutInflater)
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


        binding.linearSubmit.setOnClickListener {
            val selectedOptionId = binding.radioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
                val selectedText = selectedRadioButton.text.toString()
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
                    deleteAccountApi()
                }
            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteAccountApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        binding.txtButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.deleteAccountApi(
                getString(R.string.api_key),
                userId.toString()
            )
        call.enqueue(object : Callback<DeleteModel> {
            override fun onResponse(
                call: Call<DeleteModel>,
                response: Response<DeleteModel>
            ) {
                binding.txtButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {

                        Preferences.deleteSharedPreferences(this@DeleteAccountActivity)
                        startActivity(Intent(this@DeleteAccountActivity, LoginActivity::class.java))
                        finishAffinity()

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                binding.txtButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }


}