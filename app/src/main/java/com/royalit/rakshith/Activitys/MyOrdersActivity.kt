package com.royalit.rakshith.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityMyOrdersBinding
import com.royalit.rakshith.databinding.ActivityNotificationsBinding

class MyOrdersActivity : AppCompatActivity() {

    val binding: ActivityMyOrdersBinding by lazy {
        ActivityMyOrdersBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()
    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = "My Orders"
        binding.root.findViewById<LinearLayout>(R.id.imgBack).setOnClickListener { finish() }


    }

}