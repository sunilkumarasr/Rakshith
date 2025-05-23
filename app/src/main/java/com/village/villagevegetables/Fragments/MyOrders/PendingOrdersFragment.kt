package com.village.villagevegetables.Fragments.MyOrders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.village.villagevegetables.Activitys.MyOrderDetailsActivity
import com.village.villagevegetables.Adapters.OrdersHistoryAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.OrderHistoryModel
import com.village.villagevegetables.Models.OrderHistoryResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.FragmentPendingOrdersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingOrdersFragment : Fragment() {

    private lateinit var binding: FragmentPendingOrdersBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inIt()

    }

    private fun inIt() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {
            getOrdersHistoryApi()
        }

    }

    private fun getOrdersHistoryApi() {
        ViewController.showLoading(requireActivity())
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
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
        val filteredOrders = selectedOrdersList.filter { it.deliveryStatus == "1" || it.deliveryStatus == "2" || it.deliveryStatus == "3"}
        if (filteredOrders.isNotEmpty()) {
            binding.recyclerview.apply {
                layoutManager = GridLayoutManager(requireActivity(), 1)
                adapter = OrdersHistoryAdapter(requireActivity(), filteredOrders) { item ->
                    val intent = Intent(requireActivity(), MyOrderDetailsActivity::class.java).apply {
                        putExtra("orderId", item.orderId)
                    }
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }
            }
        } else {
            binding.linearNoData.visibility = View.VISIBLE
        }
    }

}