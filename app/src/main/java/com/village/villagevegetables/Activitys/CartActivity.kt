package com.village.villagevegetables.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Adapters.CartAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddtoCartResponse
import com.village.villagevegetables.Models.DeleteCartResponse
import com.village.villagevegetables.Models.UpdateCartResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityCartBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity(), CartAdapter.ProductItemClick,
    CartAdapter.CartItemQuantityChangeListener {

    val binding: ActivityCartBinding by lazy {
        ActivityCartBinding.inflate(layoutInflater)
    }

    lateinit var cartItemQuantityChangeListener: CartAdapter.CartItemQuantityChangeListener
    lateinit var productItemClick: CartAdapter.ProductItemClick

    //Products list
    var cartItemsList: List<CartItems> = ArrayList()


    var TotalPrice: Double = 0.0
    private var TotalFinalPrice: String = ""

    //cartCount
    var quantity = 1
    var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        cartItemQuantityChangeListener = this@CartActivity
        productItemClick = this@CartActivity

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.root.findViewById<LinearLayout>(R.id.imgBack).startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCartApi()
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            val intent = Intent(this@CartActivity, CheckOutActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(this@CartActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }

        binding.txtSelectPromoCode.setOnClickListener {
            val animations = ViewController.animation()
            binding.txtSelectPromoCode.startAnimation(animations)
            promoCodeDialog()
        }

        binding.linearOrderNote.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearOrderNote.startAnimation(animations)
            orderNoteDialog()
        }

    }


    private fun getCartApi() {
        binding.shimmerLoading.visibility = View.VISIBLE
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getCartApi(
                getString(R.string.api_key),
                userId.toString(),
            )
        call.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                binding.shimmerLoading.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        cartItemsList = response.body()?.ResponseCartList!!
                        if (cartItemsList.size > 0) {
                            getTotalPrice(cartItemsList)
                            binding.relativeData.visibility = View.VISIBLE
                            binding.linearNoData.visibility = View.GONE

                            DataSet()
                        } else {
                            binding.relativeData.visibility = View.GONE
                            binding.linearNoData.visibility = View.VISIBLE
                        }
                    } else {
                        binding.relativeData.visibility = View.GONE
                        binding.linearNoData.visibility = View.VISIBLE
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                    binding.relativeData.visibility = View.GONE
                    binding.linearNoData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                binding.shimmerLoading.visibility = View.GONE
                binding.relativeData.visibility = View.GONE
                binding.linearNoData.visibility = View.VISIBLE
                Log.e("onFailure",t.message.toString())
            }
        })

    }
    private fun DataSet() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CartActivity)
        binding.recyclerview.adapter = CartAdapter(this@CartActivity, cartItemsList, this@CartActivity, cartItemQuantityChangeListener)
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPrice(cartItemsList: List<CartItems>) {
        try {
            TotalPrice = 0.0
            for (i in cartItemsList.indices) {
                try {
                    Log.e("cart_quantity_",cartItemsList[i].cart_quantity.toString())
                    TotalPrice = TotalPrice + cartItemsList[i].offer_price
                        .toDouble() * cartItemsList[i].cart_quantity.toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.txtItems.text = "Items ("+cartItemsList.size.toString()+")"
            binding.txtItemsPrice.text = "\u20b9 $TotalPrice"
            binding.txtOrderAmount.text = "\u20b9 $TotalPrice"
            binding.txtTotalPrice.text = "\u20b9 $TotalPrice"
            TotalFinalPrice = TotalPrice.toString()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onProductItemClick(itemsData: CartItems) {

    }
    override fun onAddToCartClicked(itemsData: CartItems, cartQty: String, isAdd: Int) {
        if (isAdd == 0) {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                addToCart(itemsData, cartQty)
            }
        } else{
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                updateCart(itemsData, cartQty)
            }
        }
    }

    private fun addToCart(itemsData: CartItems, cartQty: String) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.addToCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData.products_id.toString(),
                "1"
            )
        call.enqueue(object : Callback<AddtoCartResponse> {
            override fun onResponse(
                call: Call<AddtoCartResponse>,
                response: Response<AddtoCartResponse>
            ) {
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
                try {
                    if (response.isSuccessful) {
                        val res = response.body()

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<AddtoCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
            }
        })

    }

    private fun updateCart(itemsData: CartItems, cartQty: String) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.upDateCartApi(
                getString(R.string.api_key),
                userId.toString(),
                itemsData.products_id.toString(),
                cartQty,
            )
        call.enqueue(object : Callback<UpdateCartResponse> {
            override fun onResponse(
                call: Call<UpdateCartResponse>,
                response: Response<UpdateCartResponse>
            ) {
                cartCountUpdate()
                try {
                    if (response.isSuccessful) {
                        val res = response.body()

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<UpdateCartResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
                    getCartApi()
                }
            }
        })

    }

    override fun onQuantityChanged(cartItem: CartItems, newQuantity: Int) {
//        val index = cartItemsList.indexOfFirst { it.product_id == cartItem.product_id }
//        if (index != -1) {
//            cartItemsList[index].cart_quantity = newQuantity.toString() // Now it's mutable
//            getTotalPrice(cartItemsList)
//        }
    }

    //delete item in cart
    override fun onDeleteCartItem(cartItem: CartItems) {
        deletePopup(cartItem)
    }
    @SuppressLint("InflateParams")
    private fun deletePopup(cartItem: CartItems) {
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

        val buttonCancel = customView.findViewById<TextView>(R.id.buttonCancel)
        val buttonOk = customView.findViewById<TextView>(R.id.buttonOk)

        buttonCancel.setOnClickListener {
            val animations = ViewController.animation()
            buttonCancel.startAnimation(animations)
            dialog.dismiss()
        }
        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            buttonOk.startAnimation(animations)
            dialog.dismiss()
            removeFromCartApi(cartItem)
        }

        // Show the dialog
        dialog.show()
    }
    private fun removeFromCartApi(cartItem: CartItems) {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.removeFromCartApi(
                getString(R.string.api_key),
                userId.toString(),
                cartItem.product_id,
                cartItem.id
            )
        call.enqueue(object : Callback<DeleteCartResponse> {
            override fun onResponse(
                call: Call<DeleteCartResponse>,
                response: Response<DeleteCartResponse>
            ) {
                getCartApi()
                try {
                    if(response.isSuccessful) {
                        val res = response.body()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                getCartApi()
                Log.e("onFailure",t.message.toString())
            }
        })
    }


    private fun cartCountUpdate() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.getCartApi(
                getString(R.string.api_key),
                userId.toString(),
            )
        call.enqueue(object : Callback<CartListResponse> {
            override fun onResponse(
                call: Call<CartListResponse>,
                response: Response<CartListResponse>
            ) {
                try {
                    if (response.isSuccessful) {
                        cartItemsList = response.body()?.ResponseCartList!!
                        if (cartItemsList.size > 0) {
                            getTotalPrice(cartItemsList)

                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })

    }

    //note Order
    private fun orderNoteDialog() {
        val dialog = Dialog(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.add_note_dialog, null)
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

        // Get Views
        val editNote = customView.findViewById<EditText>(R.id.editNote)
        val buttonCancel = customView.findViewById<TextView>(R.id.buttonCancel)
        val buttonOk = customView.findViewById<TextView>(R.id.buttonOk)

        if (!binding.txtNote.text.toString().equals("")){
            editNote.setText(binding.txtNote.text.toString())
        }

        buttonOk.setOnClickListener {
            if (editNote.text.toString().trim().isEmpty()) {
                ViewController.customToast(this@CartActivity, "Please enter your note")
            }else{
                if (!editNote.text.toString().equals("")){
                    binding.txtNote.text = editNote.text.toString()
                    binding.txtNoteButton.text = getString(R.string.editNote)
                }
                dialog.dismiss()
            }
        }

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    //promoCode
    private fun promoCodeDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@CartActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_promocode, null)
        bottomSheetDialog.setContentView(view)

        val buttonOk = view.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}