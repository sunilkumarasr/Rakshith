package com.village.villagevegetables.Activitys

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.village.villagevegetables.Activitys.CartActivity
import com.village.villagevegetables.Activitys.SplashActivity
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Adapters.CartAdapter
import com.village.villagevegetables.Adapters.CheckInAddressAdapter
import com.village.villagevegetables.Adapters.CheckInDialogAddressAdapter
import com.village.villagevegetables.Adapters.CheckOutProductsAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddAddressModel
import com.village.villagevegetables.Models.AddressModel
import com.village.villagevegetables.Models.AddressModelResponse
import com.village.villagevegetables.Models.AreaModel
import com.village.villagevegetables.Models.CityModel
import com.village.villagevegetables.Models.PlaceorderModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityPlaceOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import kotlin.toString

class CheckOutActivity : AppCompatActivity() {

    val binding: ActivityPlaceOrderBinding by lazy {
        ActivityPlaceOrderBinding.inflate(layoutInflater)
    }

    //address
    var addressItemsList: List<AddressModelResponse> = ArrayList()
    var addressListStatus = false

    //Products list
    var cartItemsList: List<CartItems> = ArrayList()
    var TotalPrice: Double = 0.0
    private var TotalFinalPrice: String = ""

    lateinit var cityName: String
    lateinit var areaName: String


    lateinit var productsIDS: String
    lateinit var productsQtyS: String

    var note: String = ""
    var promoCodePrice: String = ""
    var selectedAddress: String = ""

    private var deliveryChargePrice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        note = intent.getStringExtra("note").toString()
        promoCodePrice = intent.getStringExtra("promoCodePrice").toString()

        inIts()

    }

    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        //address
        binding.linearAddAddress.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearAddAddress.startAnimation(animations)
            addAddressDialog()
        }
        binding.txtChangeAddress.setOnClickListener {
            val animations = ViewController.animation()
            binding.txtChangeAddress.startAnimation(animations)
            AddressListDialog()
        }


        binding.linearPayment.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearPayment.startAnimation(animations)
            if (addressListStatus) {
                //orderSuccessPopup()
                productsIDS = ""
                productsQtyS = ""
                cartItemsList.forEach({
                    if (productsIDS.isEmpty())
                        productsIDS = it.products_id.toString()
                    else
                        productsIDS = productsIDS + "##" + it.products_id

                    if (productsQtyS.isEmpty())
                        productsQtyS = it.cart_quantity.toString()
                    else
                        productsQtyS = productsQtyS + "##" + it.cart_quantity
                })
                placeOrderSuccessApi()

            } else {
                ViewController.showToast(applicationContext, "Please add your address")
            }
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            addressListApi()
            getCartApi()
        }


    }

    //cart List
    private fun getCartApi() {
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
                        var cartList: List<CartItems> = ArrayList()
                        cartList = response.body()?.ResponseCartList ?: emptyList()
                        //stock check
                        cartItemsList = cartList.filter { it.stock.toInt() != 0 }
                        if (cartItemsList.size > 0) {
                            getTotalPrice(cartItemsList)
                            DataSet()
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure", e.message.toString())
                }
            }

            override fun onFailure(call: Call<CartListResponse>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }
        })
    }

    private fun DataSet() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@CheckOutActivity)
        binding.recyclerview.adapter = CheckOutProductsAdapter(this@CheckOutActivity, cartItemsList)
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPrice(cartItemsList: List<CartItems>) {
        try {
            TotalPrice = 0.0
            for (i in cartItemsList.indices) {
                try {
                    Log.e("cart_quantity_", cartItemsList[i].cart_quantity.toString())
                    TotalPrice = TotalPrice + cartItemsList[i].offer_price
                        .toDouble() * cartItemsList[i].cart_quantity.toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val minAmount =
                Preferences.loadStringValue(this@CheckOutActivity, Preferences.minAmount, "")
            minAmount?.toInt()?.let {
                if (it <= TotalPrice) {
                    deliveryChargePrice = ""
                    binding.txtDeliveryCharge.text = "FREE"
                    binding.txtItems.text = "Items (" + cartItemsList.size.toString() + ")"
                    binding.txtItemsPrice.text = "₹" + TotalPrice
                    binding.txtTotalPrice.text = "₹" + TotalPrice
                    TotalFinalPrice = TotalPrice.toString()
                } else {
                    deliveryChargePrice = "20"
                    binding.txtDeliveryCharge.text = "₹" + deliveryChargePrice
                    TotalPrice = (TotalPrice + deliveryChargePrice.toInt())
                    binding.txtItems.text = "Items (" + cartItemsList.size.toString() + ")"
                    binding.txtItemsPrice.text = "₹" + (TotalPrice - 20)
                    binding.txtTotalPrice.text = "₹" + TotalPrice
                    TotalFinalPrice = TotalPrice.toString()
                }
            }


        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    //address list
    private fun addressListApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.addressListApi(getString(R.string.api_key), userId.toString())
        call.enqueue(object : Callback<AddressModel> {
            override fun onResponse(call: Call<AddressModel>, response: Response<AddressModel>) {
                addressListStatus = false
                try {
                    if (response.isSuccessful) {
                        if (!response.body()?.response!!.isEmpty()) {
                            addressListStatus = true
                            binding.linearAddressList.visibility = View.VISIBLE
                            binding.linearAddAddress.visibility = View.GONE
                            //clear list
                            addressItemsList = mutableListOf()
                            addressItemsList = response.body()?.response!!
                            DataSet(addressItemsList)
                        } else {
                            binding.linearAddressList.visibility = View.GONE
                            binding.linearAddAddress.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.linearAddressList.visibility = View.GONE
                    binding.linearAddAddress.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<AddressModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                addressListStatus = false
                binding.linearAddressList.visibility = View.GONE
                binding.linearAddAddress.visibility = View.VISIBLE
            }
        })
    }

    private fun DataSet(addressList: List<AddressModelResponse>) {

        // Selected address set
        val newAddressList = mutableListOf<AddressModelResponse>()
        val addressPosition =
            Preferences.loadStringValue(applicationContext, Preferences.addressPosition, "")
        if (!addressPosition.equals("")) {
            val position = addressPosition?.toIntOrNull()
            if (position != null && position in addressList.indices) {
                newAddressList.add(addressList[position])
                selectedAddress =
                    addressList[position].name + ", " + addressList[position].city + ", " + addressList[position].area + ", " + addressList[position].landmark + ", " + addressList[position].mobileNo + ", " + addressList[position].alternateMobileNumber
            }
        } else {
            newAddressList.add(addressList[0])
            selectedAddress =
                addressList[0].name + ", " + addressList[0].city + ", " + addressList[0].area + ", " + addressList[0].landmark + ", " + addressList[0].mobileNo + ", " + addressList[0].alternateMobileNumber
        }

        // Set up RecyclerView
        binding.recyclerviewAddress.layoutManager = LinearLayoutManager(this@CheckOutActivity)
        binding.recyclerviewAddress.adapter =
            CheckInAddressAdapter(this@CheckOutActivity, newAddressList) { item ->
                // Add your item click logic here
            }
    }

    //address Dialog
    private fun AddressListDialog() {
        val bottomSheetDialog =
            BottomSheetDialog(this@CheckOutActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_addresslist, null)
        bottomSheetDialog.setContentView(view)

        val linearAdd = view.findViewById<LinearLayout>(R.id.linearAdd)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val linearRecyclerView = view.findViewById<LinearLayout>(R.id.linearRecyclerView)
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)


        //address list
        progressBar.visibility = View.VISIBLE
        linearRecyclerView.visibility = View.GONE
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.addressListApi(getString(R.string.api_key), userId.toString())
        call.enqueue(object : Callback<AddressModel> {
            override fun onResponse(call: Call<AddressModel>, response: Response<AddressModel>) {
                progressBar.visibility = View.GONE
                linearRecyclerView.visibility = View.VISIBLE
                try {
                    if (response.isSuccessful) {
                        if (!response.body()?.response!!.isEmpty()) {
                            // Set up RecyclerView
                            recyclerview.layoutManager = LinearLayoutManager(this@CheckOutActivity)
                            recyclerview.adapter = CheckInDialogAddressAdapter(
                                this@CheckOutActivity,
                                response.body()?.response!!
                            ) { item ->
                                // Add your item click logic here
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    linearRecyclerView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<AddressModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                progressBar.visibility = View.GONE
                linearRecyclerView.visibility = View.GONE
            }
        })

        linearSubmit.setOnClickListener {
            bottomSheetDialog.dismiss()
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.showToast(applicationContext, "Please check your connection ")
            } else {
                addressListApi()
            }
        }

        linearAdd.setOnClickListener {
            val animations = ViewController.animation()
            linearAdd.startAnimation(animations)
            bottomSheetDialog.dismiss()
            addAddressDialog()
        }

        bottomSheetDialog.show()
    }

    private fun addAddressDialog() {
        val bottomSheetDialog =
            BottomSheetDialog(this@CheckOutActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_addaddress, null)
        bottomSheetDialog.setContentView(view)

        val nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        val mobileEdit = view.findViewById<EditText>(R.id.mobileEdit)
        val alternateMobileEdit = view.findViewById<EditText>(R.id.alternateMobileEdit)
        val areaEdit = view.findViewById<EditText>(R.id.areaEdit)
        val spinnerCity = view.findViewById<Spinner>(R.id.spinnerCity)
        val spinnerArea = view.findViewById<Spinner>(R.id.spinnerArea)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)


        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCityListApi(spinnerCity, spinnerArea)
        }


        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            linearSubmit.startAnimation(animations)

            val name = nameEdit.text.trim().toString()
            val mobile = mobileEdit.text.trim().toString()
            val alternateMobile = alternateMobileEdit.text.trim().toString()
            val area = areaEdit.text.trim().toString()

            ViewController.hideKeyBoard(this@CheckOutActivity)
            if (name.isEmpty()) {
                ViewController.customToast(applicationContext, "Enter name")
                return@setOnClickListener
            }
            if (mobile.isEmpty()) {
                ViewController.customToast(applicationContext, "Enter mobile")
                return@setOnClickListener
            }
            if (alternateMobile.isEmpty()) {
                ViewController.customToast(applicationContext, "Enter alternate mobile")
                return@setOnClickListener
            }
            if (mobile.equals(alternateMobile)) {
                ViewController.customToast(
                    applicationContext,
                    "Mobile number and alternate mobile number both are same"
                )
                return@setOnClickListener
            }
            if (area.isEmpty()) {
                ViewController.customToast(applicationContext, "Enter area")
                return@setOnClickListener
            }
            if (cityName.isEmpty()) {
                ViewController.customToast(applicationContext, "please select city")
                return@setOnClickListener
            }
            if (areaName.isEmpty()) {
                ViewController.customToast(applicationContext, "please select area")
                return@setOnClickListener
            }

            if (!ViewController.validateMobile(mobile)) {
                ViewController.customToast(applicationContext, "Enter valid mobile")
                return@setOnClickListener
            }

            if (!ViewController.validateMobile(alternateMobile)) {
                ViewController.customToast(applicationContext, "Enter valid alternate mobile")
                return@setOnClickListener
            }

            val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.addAddressApi(
                    getString(R.string.api_key),
                    userId.toString(),
                    name,
                    mobile,
                    alternateMobile,
                    area,
                    cityName,
                    areaName
                )
            call.enqueue(object : Callback<AddAddressModel> {
                override fun onResponse(
                    call: Call<AddAddressModel>,
                    response: Response<AddAddressModel>
                ) {
                    bottomSheetDialog.dismiss()
                    try {
                        if (response.isSuccessful) {
                            if (response.body()?.message.equals("Success")) {
                                val result = (addressItemsList.size).toString()
                                Preferences.saveStringValue(
                                    this@CheckOutActivity,
                                    Preferences.addressPosition,
                                    result
                                )
                                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                                    ViewController.showToast(
                                        applicationContext,
                                        "Please check your connection "
                                    )
                                } else {
                                    addressListApi()
                                }
                            }
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                        Log.e("t_", e.message.toString())
                    }
                }

                override fun onFailure(call: Call<AddAddressModel>, t: Throwable) {
                    Log.e("t_", t.message.toString())
                    bottomSheetDialog.dismiss()
                }
            })
        }
        bottomSheetDialog.show()
    }

    private fun getCityListApi(spinnerCity: Spinner, spinnerArea: Spinner) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCityListApi(getString(R.string.api_key))
        call.enqueue(object : Callback<CityModel> {
            override fun onResponse(call: Call<CityModel>, response: Response<CityModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val stateList = response.body()?.response ?: emptyList()
                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@CheckOutActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.cityName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerCity.adapter = adapter

                            // Optional: Handle item selection
                            spinnerCity.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val selectedState = stateList[position]
                                        val stateId = selectedState.cityId
                                        cityName = selectedState.cityName

                                        areaName = ""
                                        getAreaListApi(stateId, spinnerArea)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                    }
                                }
                        }

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }

            override fun onFailure(call: Call<CityModel>, t: Throwable) {
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }

    private fun getAreaListApi(stateId: String, spinnerArea: Spinner) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getAreaListApi(getString(R.string.api_key), stateId)
        call.enqueue(object : Callback<AreaModel> {
            override fun onResponse(call: Call<AreaModel>, response: Response<AreaModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val stateList = response.body()?.response ?: emptyList()
                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@CheckOutActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.areaName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerArea.adapter = adapter

                            // Optional: Handle item selection
                            spinnerArea.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        val selectedState = stateList[position]
                                        val city_id = selectedState.areaId
                                        areaName = selectedState.areaName
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                    }
                                }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }

            override fun onFailure(call: Call<AreaModel>, t: Throwable) {
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }


    private fun placeOrderSuccessApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.placeOrderSuccessApi(
                getString(R.string.api_key),
                userId.toString(),
                productsIDS,
                productsQtyS,
                note,
                TotalFinalPrice,
                selectedAddress,
                promoCodePrice,
                deliveryChargePrice,
            )

        call.enqueue(object : Callback<PlaceorderModel> {
            override fun onResponse(
                call: Call<PlaceorderModel>,
                response: Response<PlaceorderModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        orderSuccessPopup()
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure", e.message.toString())
                    orderFailedPopup()
                }
            }

            override fun onFailure(call: Call<PlaceorderModel>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
                orderFailedPopup()
            }
        })
    }

    private fun orderSuccessPopup() {
        val dialog = Dialog(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.payment_success_popup, null)
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

        //set Sound
        var mediaPlayer: MediaPlayer? = null
        mediaPlayer = MediaPlayer.create(this, R.raw.success_file)
        mediaPlayer?.start()

        val linearSubmit = customView.findViewById<LinearLayout>(R.id.linearSubmit)
        val linearHome = customView.findViewById<LinearLayout>(R.id.linearHome)
        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            linearSubmit.startAnimation(animations)
            val intent = Intent(this@CheckOutActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)

            dialog.dismiss()
        }
        linearHome.setOnClickListener {
            val animations = ViewController.animation()
            linearHome.startAnimation(animations)
            val intent = Intent(this@CheckOutActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    private fun orderFailedPopup() {
        val dialog = Dialog(this)
        val customView = LayoutInflater.from(this).inflate(R.layout.payment_failed_popup, null)
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

        val linearSubmit = customView.findViewById<LinearLayout>(R.id.linearSubmit)
        val linearHome = customView.findViewById<LinearLayout>(R.id.linearHome)

        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            linearSubmit.startAnimation(animations)
            val intent = Intent(this@CheckOutActivity, DashBoardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
            dialog.dismiss()
        }
        linearHome.setOnClickListener {
            val animations = ViewController.animation()
            linearHome.startAnimation(animations)

            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}