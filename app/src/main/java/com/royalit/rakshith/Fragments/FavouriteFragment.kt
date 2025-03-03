package com.royalit.rakshith.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.royalit.rakshith.Activitys.ProductsDetailsActivity
import com.royalit.rakshith.Adapters.FavouriteAdapter
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.FavouriteModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.FragmentFavouriteBinding

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
        favouriteList.add(FavouriteModel(R.drawable.beets_ic, "Tomato ahfdsa df jasg fja", "₹800","",4))
        favouriteList.add(FavouriteModel(R.drawable.califlower_ic, "Cabbage", "₹400","",4))
        favouriteList.add(FavouriteModel(R.drawable.green_leafy_ic, "Bangala", "₹500","",4))
        favouriteList.add(FavouriteModel(R.drawable.beets_ic, "Capsicum", "₹800","",4))
        favouriteList.add(FavouriteModel(R.drawable.califlower_ic, "Green Mirchi", "₹200","",4))
        favouriteList.add(FavouriteModel(R.drawable.beets_ic, "Onion", "₹900","",4))
        favouriteList.add(FavouriteModel(R.drawable.green_leafy_ic, "CCarrot", "₹300","",4))

        // Set the adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(activity) // This sets the RecyclerView to display in a vertical list
        binding.recyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        favouriteAdapter = FavouriteAdapter(favouriteList) { selectedItem ->
            val intent = Intent(requireActivity(), ProductsDetailsActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.recyclerview.adapter = favouriteAdapter
        binding.recyclerview.setHasFixedSize(true)


    }

}