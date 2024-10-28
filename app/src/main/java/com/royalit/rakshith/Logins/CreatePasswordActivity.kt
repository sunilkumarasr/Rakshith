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
import com.royalit.rakshith.databinding.ActivityCreatePasswordBinding
import com.royalit.rakshith.databinding.ActivitySplashBinding

class CreatePasswordActivity : AppCompatActivity() {

    val binding: ActivityCreatePasswordBinding by lazy {
        ActivityCreatePasswordBinding.inflate(layoutInflater)
    }

    lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)
        email= intent.getStringExtra("email").toString()


        inits()

    }

    private fun inits() {
        binding.linearBack.setOnClickListener {
            val intent = Intent(this@CreatePasswordActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

//
//        binding.cardLogin.setOnClickListener {
//            if(!ViewController.noInterNetConnectivity(applicationContext)){
//                ViewController.showToast(applicationContext, "Please check your connection ")
//            }else{
//                createApi()
//            }
//        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@CreatePasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }


}