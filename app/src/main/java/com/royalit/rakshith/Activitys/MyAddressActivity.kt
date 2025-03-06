package com.royalit.rakshith.Activitys

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.royalit.rakshith.Adapters.AddressAdapter
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.AddressModel
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityAddAddressBinding

class MyAddressActivity : AppCompatActivity() {

    val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    //Address
    private lateinit var addressAdapter: AddressAdapter
    private lateinit var addressList: ArrayList<AddressModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.cardAdd.setOnClickListener {
            val animations = ViewController.animation()
            binding.cardAdd.startAnimation(animations)
            addAddressDialog()
        }


        addressListApi()
    }

    private fun addressListApi() {

        // Populate the static list with data
        addressList = ArrayList()
        addressList.add(AddressModel("Home", "House 10, Road 5, Block J, Baridhara", "534146"))
        addressList.add(AddressModel("Office", "Road 5, Block J, Baridhara", "534211"))

        // Set the adapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this@MyAddressActivity, LinearLayoutManager.VERTICAL, false)
        addressAdapter = AddressAdapter(addressList) { selectedItem, type ->

        }
        binding.recyclerview.adapter = addressAdapter
        binding.recyclerview.setHasFixedSize(true)

    }


    private fun addAddressDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@MyAddressActivity)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_addaddress, null)
        bottomSheetDialog.setContentView(view)

        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)

        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            linearSubmit.startAnimation(animations)
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