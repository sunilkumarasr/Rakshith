package com.royalit.rakshith.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.rakshith.Adapters.Cart.CartListResponse
import com.royalit.rakshith.Adapters.CartAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.CartModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    val binding: ActivityCartBinding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }

    //Products
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartList: ArrayList<CartModel>

    //cartCount
    var quantity = 1
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
        binding.root.findViewById<TextView>(R.id.txtTitle).text = getString(R.string.cart)
        binding.root.findViewById<LinearLayout>(R.id.imgBack).setOnClickListener {
            val animations = ViewController.animation()
            binding.root.findViewById<LinearLayout>(R.id.imgBack).startAnimation(animations)
            finish()
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCart()
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            val intent = Intent(this@CartActivity, CheckOutActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

    }


    private fun getCart() {
        binding.progressBar.visibility = View.VISIBLE
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getCartList(
                getString(R.string.api_key),
                userId.toString(),
            )
        call.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {

                    } else {
                        ViewController.showToast(applicationContext, "Invalid Mobile Number")
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })

    }


    @SuppressLint("InflateParams")
    private fun deletePopup() {
        val dialog = Dialog(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.item_delete_conformation_popup, null)
        dialog.setContentView(customView)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val layoutParams = dialog.window?.attributes
        layoutParams?.width =
            (resources.displayMetrics.widthPixels * 0.9).toInt() // 90% of screen width
        dialog.window?.attributes = layoutParams

        val btnDelete = customView.findViewById<Button>(R.id.btnDelete)

        btnDelete.setOnClickListener {
            val animations = ViewController.animation()
            btnDelete.startAnimation(animations)
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

}