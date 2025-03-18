package com.village.villagevegetables.Activitys

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityMyWalletBinding

class MyWalletActivity : AppCompatActivity() {

    val binding: ActivityMyWalletBinding by lazy {
        ActivityMyWalletBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        inIts()

    }


    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(this@MyWalletActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        //myWalletApi()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}