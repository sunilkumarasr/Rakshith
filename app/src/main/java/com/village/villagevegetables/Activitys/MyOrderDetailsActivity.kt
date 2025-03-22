package com.village.villagevegetables.Activitys

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.village.villagevegetables.Adapters.OrdersDetailsAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.OrderHistoryModel
import com.village.villagevegetables.Models.OrderHistoryResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityMyOrderDetailsBinding
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
                binding.txtItems.text = getString(R.string.Items)+ " " + ordersItemsList.size
                binding.txtItemsPrice.text = "₹" + order.grandTotal
                binding.txtTotalAmount.text = "₹" + order.grandTotal
                binding.txtAddress.text = "Address : "+order.billingAddress
                binding.txtName.text = "Name : "+order.fullName
                binding.txtMobile.text = "Mobile : "+order.mobile
                binding.txtEmail.text = "Email : "+order.email
                binding.txtOrderDate.text ="Order Date : " + order.updatedDate
                if (!order.deliveryCharge.equals("")){
                    binding.txtDeliveryCharge.text = "₹" +order.deliveryCharge
                    var sum = order.grandTotal.toDouble() + order.deliveryCharge.toDouble()
                    binding.txtOrderTotalAmount.text = "₹" + sum.toString()
                }else{
                    binding.txtDeliveryCharge.text = "Free"
                    binding.txtOrderTotalAmount.text = "₹" + order.grandTotal
                }

                // set fields
                binding.txtOrderNumber.text = order.orderNumber

                //order track bar
                if (order.deliveryStatus == "1"){
                    //view
                    binding.view1copy.setBackgroundResource(R.color.lightGray)
                    binding.view2.setBackgroundResource(R.color.lightGray)
                    binding.view2copy.setBackgroundResource(R.color.lightGray)
                    binding.view3.setBackgroundResource(R.color.lightGray)
                    binding.view3copy.setBackgroundResource(R.color.lightGray)
                    //round
                    val newTintColor = ContextCompat.getColor(this, R.color.lightGray)
                    binding.img2.imageTintList = ColorStateList.valueOf(newTintColor)
                    binding.img3.imageTintList = ColorStateList.valueOf(newTintColor)
                    binding.img4.imageTintList = ColorStateList.valueOf(newTintColor)
                }
                if (order.deliveryStatus == "2"){
                    //view
                    binding.view2copy.setBackgroundResource(R.color.lightGray)
                    binding.view3.setBackgroundResource(R.color.lightGray)
                    binding.view3copy.setBackgroundResource(R.color.lightGray)
                    //round
                    val newTintColor = ContextCompat.getColor(this, R.color.lightGray)
                    binding.img3.imageTintList = ColorStateList.valueOf(newTintColor)
                    binding.img4.imageTintList = ColorStateList.valueOf(newTintColor)
                }
                if (order.deliveryStatus == "3"){
                    //view
                    binding.view3copy.setBackgroundResource(R.color.lightGray)
                    //round
                    val newTintColor = ContextCompat.getColor(this, R.color.lightGray)
                    binding.img4.imageTintList = ColorStateList.valueOf(newTintColor)
                }

                if (order.deliveryStatus == "4"){
                    //Default green
                }

                if (order.deliveryStatus == "5"){
                    binding.linearOrderTrack.visibility = View.GONE
                    binding.linearOrderCancelTrack.visibility = View.VISIBLE
                }

                break
            }
        }

    }

}