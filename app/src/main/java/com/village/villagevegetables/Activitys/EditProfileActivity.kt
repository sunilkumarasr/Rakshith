package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.ProfileModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityEditProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
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

        val name = Preferences.loadStringValue(this@EditProfileActivity, Preferences.name, "")
        val mobileNumber = Preferences.loadStringValue(this@EditProfileActivity, Preferences.mobileNumber, "")
        val email = Preferences.loadStringValue(this@EditProfileActivity, Preferences.email, "")
        binding.nameEdit.setText(name.toString())
        binding.mobileEdit.setText(mobileNumber.toString())
        binding.emailEdit.setText(email.toString())

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            //getProfileApi()
        }

        binding.linearSubmit.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                updateProfileApi()
            }
        }

    }

    private fun getProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        ViewController.showLoading(this@EditProfileActivity)
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getProfileApi(getString(R.string.api_key),userId.toString())
        call.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(call: Call<ProfileModel>, response: Response<ProfileModel>) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        val selectedServicesList = response.body()
                        //empty
                        if (selectedServicesList != null) {
                            if (!selectedServicesList.status.equals("true")) {
                                //DataSet(selectedServicesList)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }
            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
                ViewController.hideLoading()
            }
        })
    }


    private fun updateProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "")
        val name = binding.nameEdit.text?.trim().toString()
        val email = binding.emailEdit.text?.trim().toString()
        val mobile = binding.mobileEdit.text?.trim().toString()
        ViewController.hideKeyBoard(this@EditProfileActivity )

        if (name.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter your name")
            return
        }
        if (email.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter email")
            return
        }
        if (mobile.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter mobile number")
            return
        }


        ViewController.showLoading(this@EditProfileActivity)

        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.updateProfileApi(
                getString(R.string.api_key),
                userId.toString(),
                name,
                mobile,
                email
            )

        call.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(
                call: Call<ProfileModel>,
                response: Response<ProfileModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        Preferences.saveStringValue(this@EditProfileActivity, Preferences.name,binding.nameEdit.text.toString())
                        Preferences.saveStringValue(this@EditProfileActivity, Preferences.mobileNumber,binding.mobileEdit.text.toString())
                        Preferences.saveStringValue(this@EditProfileActivity, Preferences.email,binding.emailEdit.text.toString())

                        val intent = Intent(this@EditProfileActivity, DashBoardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                ViewController.hideLoading()
                ViewController.customToast(applicationContext, "Register Failed")
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}