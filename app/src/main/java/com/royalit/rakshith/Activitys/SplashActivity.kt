package com.royalit.rakshith.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Logins.LoginActivity
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        inits()

    }

    private fun inits() {
        val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")

        Handler(Looper.getMainLooper()).postDelayed({
            if (loginCheck.equals("Login")) {
                startActivity(Intent(this@SplashActivity, DashBoardActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
        }, 3000)
    }


}