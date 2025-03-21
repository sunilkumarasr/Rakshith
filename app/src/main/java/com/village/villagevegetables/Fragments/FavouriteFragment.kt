package com.village.villagevegetables.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.village.villagevegetables.Activitys.DashBoardActivity
import com.village.villagevegetables.Adapters.FavouriteAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.FavouriteModel
import com.village.villagevegetables.Models.FavouriteResponse
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.FragmentFavouriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    //Favourite
    private lateinit var favouriteList: MutableList<FavouriteResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
        } else {
            getFavouriteApi()
        }

        binding.linearSubmitGotoHome.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmitGotoHome.startAnimation(animations)
            val intent = Intent(requireActivity(), DashBoardActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(0, 0)
        }

    }

    private fun getFavouriteApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
        binding.shimmerLoading.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getFavouriteApi(getString(R.string.api_key), userId.toString())
        call.enqueue(object : Callback<FavouriteModel> {
            override fun onResponse(call: Call<FavouriteModel>, response: Response<FavouriteModel>) {
                binding.shimmerLoading.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        favouriteList = response.body()?.response!!
                        if (favouriteList.isEmpty()){
                            binding.linearNoData.visibility = View.VISIBLE
                        }else{
                            DataSet()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.linearNoData.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<FavouriteModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                binding.shimmerLoading.visibility = View.GONE
                binding.linearNoData.visibility = View.VISIBLE
            }
        })
    }

    private fun DataSet() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireActivity()).apply {
            isSmoothScrollbarEnabled = true  // Enable smooth scrolling
        }

        binding.recyclerview.setHasFixedSize(true)

        binding.recyclerview.adapter = FavouriteAdapter(requireActivity(), favouriteList)
    }



}