package com.royalit.rakshith.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCartBinding
import com.royalit.rakshith.databinding.ActivityEditProfileBinding

class CartActivity : AppCompatActivity() {

    val binding: ActivityCartBinding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
//        binding.root.findViewById<TextView>(R.id.txtTitle).text = "Cart"
//        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }

}