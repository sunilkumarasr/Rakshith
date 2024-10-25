package com.royalit.rakshith.Activitys

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityEditAddressBinding

class EditAddressActivity : AppCompatActivity() {

    val binding: ActivityEditAddressBinding by lazy {
        ActivityEditAddressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_address)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = getString(R.string.editaddress)
        binding.root.findViewById<ImageView>(R.id.imgBack).setOnClickListener { finish() }

    }

}