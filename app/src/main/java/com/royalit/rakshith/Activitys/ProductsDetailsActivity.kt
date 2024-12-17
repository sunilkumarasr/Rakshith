package com.royalit.rakshith.Activitys

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityProductsDetailsBinding

class ProductsDetailsActivity : AppCompatActivity() {

    val binding: ActivityProductsDetailsBinding by lazy {
        ActivityProductsDetailsBinding.inflate(layoutInflater)
    }

    var quantity = 1
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }


    private fun inits() {
        binding.root.findViewById<LinearLayout>(R.id.imgBack).setOnClickListener {
            val animations = ViewController.animation()
            binding.root.findViewById<LinearLayout>(R.id.imgBack).startAnimation(animations)
            finish()
        }
        
        binding.AddFavourite.setOnClickListener {
            val animations = ViewController.animation()
            binding.AddFavourite.startAnimation(animations)
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.imgFav.setImageResource(R.drawable.favadd)
            } else {
                binding.imgFav.setImageResource(R.drawable.favorite_ic)
            }
        }

        binding.linearIncrement.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearIncrement.startAnimation(animations)
            quantity++
            binding.tvQuantity.text = quantity.toString()
        }
        binding.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearDecrement.startAnimation(animations)
            if (quantity > 1) {
                quantity--
                binding.tvQuantity.text = quantity.toString()
            }
        }

        binding.linearAddToCart.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearAddToCart.startAnimation(animations)

        }

        productDetailsSet()
    }

    private fun productDetailsSet() {

    }

}