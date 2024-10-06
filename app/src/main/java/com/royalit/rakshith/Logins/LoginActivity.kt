package com.royalit.rakshith.Logins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.rakshith.Activitys.DashBoardActivity
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()
    }

    private fun inits() {

        binding.txtForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotActivity::class.java))
        }
        binding.registerLinear.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        binding.linearSubmit.setOnClickListener {
            startActivity(Intent(this@LoginActivity, DashBoardActivity::class.java))
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}