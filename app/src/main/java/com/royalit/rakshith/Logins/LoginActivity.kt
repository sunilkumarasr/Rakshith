package com.royalit.rakshith.Logins

import android.content.Intent
import android.graphics.Bitmap.Config
import android.os.Build
import android.os.Bundle
import android.view.View
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
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}