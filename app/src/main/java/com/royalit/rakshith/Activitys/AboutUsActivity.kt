package com.royalit.rakshith.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    val binding: ActivityAboutUsBinding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
//        binding.root.findViewById<TextView>(R.id.txtTitle).text = "AboutUs"
//        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }

}