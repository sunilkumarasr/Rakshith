package com.royalit.rakshith.Logins

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityForgotBinding

class ForgotActivity : AppCompatActivity() {

    val binding: ActivityForgotBinding by lazy {
        ActivityForgotBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)


        inits()

    }

    private fun inits() {
//        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }
//
//        binding.txtLogin.setOnClickListener {
//            finish()
//        }

        binding.linearSubmit.setOnClickListener {
            startActivity(Intent(this@ForgotActivity,OTPActivity::class.java).apply {
                putExtra("email",binding.emailEdit.editableText.trim().toString())
            })
            finish()
        }

//
//        binding.cardForgot.setOnClickListener {
//            if(!ViewController.noInterNetConnectivity(applicationContext)){
//                ViewController.showToast(applicationContext, "Please check your connection ")
//            }else{
//                forgotApi()
//            }
//        }
    }


    private fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }

}