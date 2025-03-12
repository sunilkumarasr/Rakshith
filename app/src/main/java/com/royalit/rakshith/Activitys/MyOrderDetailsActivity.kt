package com.royalit.rakshith.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.royalit.rakshith.Adapters.OrdersDetailsAdapter
import com.royalit.rakshith.Adapters.OrdersHistoryAdapter
import com.royalit.rakshith.Api.RetrofitClient
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.OrderHistoryModel
import com.royalit.rakshith.Models.OrderHistoryResponse
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityCategoriesProductsListBinding
import com.royalit.rakshith.databinding.ActivityMyOrderDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOrderDetailsActivity : AppCompatActivity() {

    val binding: ActivityMyOrderDetailsBinding by lazy {
        ActivityMyOrderDetailsBinding.inflate(layoutInflater)
    }


    var orderId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        orderId = intent.getStringExtra("orderId").toString()


        inIts()

    }

    private fun inIts() {

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getOrdersHistoryApi()
        }

    }

    private fun getOrdersHistoryApi() {
        ViewController.showLoading(this@MyOrderDetailsActivity)
        val userId = Preferences.loadStringValue(this@MyOrderDetailsActivity, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getOrdersHistoryApi(getString(R.string.api_key), userId.toString() )
        call.enqueue(object : Callback<OrderHistoryModel> {
            override fun onResponse(call: Call<OrderHistoryModel>, response: Response<OrderHistoryModel>) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        val selectedServicesList = response.body()?.response
                        //empty
                        if (selectedServicesList != null) {
                            if (selectedServicesList.isNotEmpty()) {
                                DataSet(selectedServicesList)
                            }else{
                                binding.linearNoData.visibility = View.VISIBLE
                            }
                        }else{
                            binding.linearNoData.visibility = View.VISIBLE
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.linearNoData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<OrderHistoryModel>, t: Throwable) {
                ViewController.hideLoading()
                binding.linearNoData.visibility = View.VISIBLE
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }

    private fun DataSet(selectedOrdersList: List<OrderHistoryResponse>) {

        for (order in selectedOrdersList) {
            if (order.orderId == orderId) {
                val ordersItemsList = order.productDetails.toList() ?: emptyList()
                binding.recyclerview.layoutManager = GridLayoutManager(this@MyOrderDetailsActivity, 1)
                binding.recyclerview.adapter = OrdersDetailsAdapter(this@MyOrderDetailsActivity, ordersItemsList) { item ->
                    val intent = Intent(this@MyOrderDetailsActivity, ProductsDetailsActivity::class.java).apply {
                        putExtra("productsId", item.productsId)
                    }
                    startActivity(intent)
                    overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }

                //items total
                val totalItemsPrice = ordersItemsList.sumByDouble {
                    (it.price.toDoubleOrNull() ?: 0.0)
                }
                binding.txtItemsPrice.text = "₹" + totalItemsPrice.toString()
                binding.txtTotalAmount.text = "₹" + order.grandTotal
                binding.txtAddress.text =order.billingAddress
                binding.txtName.text =order.fullName
                binding.txtMobile.text =order.mobile
                binding.txtEmail.text =order.email
                binding.txtOrderTotalAmount.text = "₹" + order.grandTotal

                // set fields
                binding.txtOrderNumber.text = order.orderNumber
                break
            }
        }

    }


}