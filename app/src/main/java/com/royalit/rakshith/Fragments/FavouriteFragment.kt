package com.royalit.rakshith.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.royalit.rakshith.Activitys.ProductsListActivity
import com.royalit.rakshith.Adapters.FavouriteAdapter
import com.royalit.rakshith.Adapters.HomeProductsAdapter
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.FavouriteModel
import com.royalit.rakshith.Models.HomeProductsModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.FragmentFavouriteBinding
import com.royalit.rakshith.databinding.FragmentHomeBinding

class FavouriteFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteBinding

    //Favourite
    private lateinit var favouriteAdapter: FavouriteAdapter
    private lateinit var favouriteList: ArrayList<FavouriteModel>

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
            return
        } else {
            FavouriteApi()
        }

    }

    private fun FavouriteApi() {

        // Populate the static list with data
        favouriteList = ArrayList()
        favouriteList.add(FavouriteModel(R.drawable.tomoto_ic, "Tomato", "₹800","",4))
        favouriteList.add(FavouriteModel(R.drawable.cabage_ic, "Cabbage", "₹400","",4))
        favouriteList.add(FavouriteModel(R.drawable.bangala_ic, "Bangala", "₹500","",4))
        favouriteList.add(FavouriteModel(R.drawable.capsicom_ic, "Capsicum", "₹800","",4))
        favouriteList.add(FavouriteModel(R.drawable.mirchi_ic, "Green Mirchi", "₹200","",4))
        favouriteList.add(FavouriteModel(R.drawable.onion_ic, "Onion", "₹900","",4))
        favouriteList.add(FavouriteModel(R.drawable.carrot, "CCarrot", "₹300","",4))

        // Set the adapter
        binding.recyclerview.layoutManager = GridLayoutManager(activity, 2)
        favouriteAdapter = FavouriteAdapter(favouriteList){ selectedItem ->
            Toast.makeText(activity, "Added ${selectedItem.title} to cart", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerview.adapter = favouriteAdapter
        binding.recyclerview.setHasFixedSize(true)

    }

}