package com.village.villagevegetables.Logins

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityCreatePasswordBinding

class CreatePasswordActivity : AppCompatActivity() {

    val binding: ActivityCreatePasswordBinding by lazy {
        ActivityCreatePasswordBinding.inflate(layoutInflater)
    }

    lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        email= intent.getStringExtra("email").toString()


        inIts()

    }

    private fun inIts() {
        binding.linearBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearBack.startAnimation(animations)
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