package com.village.villagevegetables.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityEditAddressBinding

class EditAddressActivity : AppCompatActivity() {

    val binding: ActivityEditAddressBinding by lazy {
        ActivityEditAddressBinding.inflate(layoutInflater)
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


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}