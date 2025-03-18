package com.village.villagevegetables.Logins

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.village.villagevegetables.Activitys.DashBoardActivity
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.RegisterModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var passwordView: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()
    }

    private fun inIts() {


        // Toggle password visibility
        binding.passwordToggle.setOnClickListener {
            binding.passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            if (passwordView){
                passwordView = false
                // Hide the password
                binding.passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordToggle.setImageResource(R.drawable.close_eye_ic)
            }else{
                passwordView = true
                // Show the password
                binding.passwordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordToggle.setImageResource(R.drawable.open_eye_ic)
            }

        }

        binding.loginLinear.setOnClickListener {
            val animations = ViewController.animation()
            binding.loginLinear.startAnimation(animations)
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.loginLinear.startAnimation(animations)
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)

            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                registerApi()
            }

        }
    }


    private fun registerApi() {
        val name = binding.nameEdit.text?.trim().toString()
        val email = binding.emailEdit.text?.trim().toString()
        val mobile = binding.mobileEdit.text?.trim().toString()
        val password = binding.passwordEdit.text?.trim().toString()

        ViewController.hideKeyBoard(this@RegisterActivity )

        if (name.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Your Name")
            return
        }
        if (email.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Email")
            return
        }
        if (mobile.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Mobile Number")
            return
        }
        if (password.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter password")
            return
        }

        if (!ViewController.validateMobile(mobile)) {
            ViewController.customToast(applicationContext, "Enter Valid mobile number")
        }else if (!binding.checkbox.isChecked) {
            ViewController.customToast(applicationContext, "Please agree to the terms and conditions.")
        } else {
            ViewController.showLoading(this@RegisterActivity)

            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.registerApi(
                    getString(R.string.api_key),
                    name,
                    mobile,
                    email,
                    password
                )

            call.enqueue(object : Callback<RegisterModel> {
                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: Response<RegisterModel>
                ) {
                    ViewController.hideLoading()
                    try {
                        if (response.isSuccessful) {

                            if (response.body()?.status == true){
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                ViewController.customToast(applicationContext, "Register Failed")
                            }

                        } else {
                            ViewController.customToast(applicationContext, "Register Failed")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.customToast(applicationContext, "Register Failed")
                }
            })

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}