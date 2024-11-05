package com.royalit.rakshith.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.rakshith.Adapters.CartAdapter
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.CartModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCartBinding

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

        CartListApi()

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            val intent = Intent(this@CartActivity, CheckOutActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

    }


    private fun CartListApi() {

        // Populate the static list with data
        cartList = ArrayList()
        cartList.add(CartModel(R.drawable.tomoto_ic, "Tomato", "₹800","₹700",4))
        cartList.add(CartModel(R.drawable.cabage_ic, "Cabbage", "₹1000","₹700",4))
        cartList.add(CartModel(R.drawable.bangala_ic, "Bangala", "₹1500","₹700",4))
        cartList.add(CartModel(R.drawable.capsicom_ic, "Capsicum", "₹1800","₹700",4))
        cartList.add(CartModel(R.drawable.mirchi_ic, "Green Mirchi", "₹2100","₹700",4))
        cartList.add(CartModel(R.drawable.onion_ic, "Onion", "₹9100","₹700",4))
        cartList.add(CartModel(R.drawable.carrot, "Carrot", "₹3100","₹700",4))


        // Set the adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
        cartAdapter = CartAdapter(cartList){ selectedItem, type ->
            if (type.equals("Delete")){
                deletePopup()
            }
            if (type.equals("Increment")){
                quantity++
            }
            if (type.equals("Decrement")){
                if (quantity > 1) {
                    quantity--
                }
            }
            if (type.equals("")){
                val intent = Intent(this@CartActivity, ProductsDetailsActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }

        }

        binding.recyclerview.adapter = cartAdapter
        binding.recyclerview.setHasFixedSize(true)

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