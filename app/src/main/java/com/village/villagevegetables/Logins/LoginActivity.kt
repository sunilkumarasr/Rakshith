package com.village.villagevegetables.Logins

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.village.villagevegetables.Activitys.DashBoardActivity
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.LoginModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    var passwordView: Boolean = false
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        inIts()
    }

    private fun inIts() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("Fetching FCM registration token failed", task.exception.toString())
                return@OnCompleteListener
            }
            // Get new FCM registration token
            token = task.result
            Log.e("token_", token.toString())
        })

        binding.txtForgot.setOnClickListener {
            val animations = ViewController.animation()
            binding.txtForgot.startAnimation(animations)
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        binding.registerLinear.setOnClickListener {
            val animations = ViewController.animation()
            binding.registerLinear.startAnimation(animations)
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        binding.linearSubmit.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                loginApi()
            }
        }

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


    }


    private fun loginApi() {
        val email = binding.emailEdit.text?.trim().toString()
        val password = binding.passwordEdit.text?.trim().toString()

        ViewController.hideKeyBoard(this@LoginActivity )

        if (email.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter email")
            return
        }
        if (password.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter password")
            return
        }

        if (!ViewController.validateEmail(email)) {
            ViewController.customToast(applicationContext, "Enter valid email")
        } else {
            binding.txtButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.loginApi(
                    getString(R.string.api_key),
                    email,
                    password,
                    token
                )

            call.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    binding.txtButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    try {
                        if (response.isSuccessful) {

                            if (response.body()?.code==1){
                                Preferences.saveStringValue(this@LoginActivity, Preferences.userId,response.body()?.response!!.customer_id.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.name,response.body()?.response!!.full_name.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.mobileNumber,response.body()?.response!!.mobile_number.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.address,response.body()?.response!!.address.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.email,response.body()?.response!!.email_id.toString())

                                val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                ViewController.customToast(applicationContext, "Login Failed")
                            }


                        } else {
                            ViewController.customToast(applicationContext, "Invalid Mobile Number")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    binding.txtButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    ViewController.customToast(applicationContext, "Invalid Credentials")
                }
            })

        }
    }

}