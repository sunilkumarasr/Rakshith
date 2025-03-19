package com.village.villagevegetables.Activitys

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.village.villagevegetables.Adapters.AddressAdapter
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddAddressModel
import com.village.villagevegetables.Models.AddressModel
import com.village.villagevegetables.Models.AddressModelResponse
import com.village.villagevegetables.Models.AreaModel
import com.village.villagevegetables.Models.CityModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityAddAddressBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAddressActivity : AppCompatActivity(), AddressAdapter.ItemClick {

    lateinit var itemClick: AddressAdapter.ItemClick

    val binding: ActivityAddAddressBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    lateinit var cityName: String
    lateinit var areaName: String

    //update
    lateinit var updateCityName: String
    lateinit var updateAreaName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {

        itemClick = this@MyAddressActivity

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
        binding.linearAdd.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearAdd.startAnimation(animations)
            addAddressDialog()
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            addressListApi()
        }

    }

    private fun addressListApi() {
        binding.shimmerLoading.visibility = View.VISIBLE
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.addressListApi(getString(R.string.api_key), userId.toString())
        call.enqueue(object : Callback<AddressModel> {
            override fun onResponse(call: Call<AddressModel>, response: Response<AddressModel>) {
                binding.shimmerLoading.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        if (!response.body()?.response!!.isEmpty()){
                            DataSet(response.body()?.response!!)
                        }else{
                            binding.recyclerview.visibility = View.GONE
                            binding.noData.visibility = View.VISIBLE
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.recyclerview.visibility = View.GONE
                    binding.noData.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<AddressModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                binding.shimmerLoading.visibility = View.GONE
                binding.recyclerview.visibility = View.GONE
                binding.noData.visibility = View.VISIBLE
            }
        })
    }
    private fun DataSet(addressList: List<AddressModelResponse>) {
        binding.recyclerview.layoutManager = LinearLayoutManager(this@MyAddressActivity)
        binding.recyclerview.adapter = AddressAdapter(  this@MyAddressActivity, addressList, this@MyAddressActivity)
    }

    override fun onItemClick(itemsData: AddressModelResponse) {

    }

    override fun onClicked(
        itemsData: AddressModelResponse,
        type: String) {

        if (type.equals("edit")){
            updateAddressDialog(itemsData)
        }else{
            deleteAddressApi(itemsData.id)
        }

    }

    //update address
    //add address
    private fun updateAddressDialog(itemsData: AddressModelResponse) {
        val bottomSheetDialog = BottomSheetDialog(this@MyAddressActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_addaddress, null)
        bottomSheetDialog.setContentView(view)

        val nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        val mobileEdit = view.findViewById<EditText>(R.id.mobileEdit)
        val alternateMobileEdit = view.findViewById<EditText>(R.id.alternateMobileEdit)
        val areaEdit = view.findViewById<EditText>(R.id.areaEdit)
        val spinnerCity = view.findViewById<Spinner>(R.id.spinnerCity)
        val spinnerArea = view.findViewById<Spinner>(R.id.spinnerArea)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)

        nameEdit.setText(itemsData.name.toString())
        mobileEdit.setText(itemsData.mobileNo.toString())
        alternateMobileEdit.setText(itemsData.alternateMobileNumber.toString())
        areaEdit.setText(itemsData.landmark.toString())
        updateCityName = itemsData.city.toString()
        updateAreaName = itemsData.area.toString()

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCityListUpadteApi(spinnerCity, spinnerArea)
        }

        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            linearSubmit.startAnimation(animations)

            val name = nameEdit.text.trim().toString()
            val mobile = mobileEdit.text.trim().toString()
            val alternateMobile = alternateMobileEdit.text.trim().toString()
            val area = areaEdit.text.trim().toString()

            ViewController.hideKeyBoard(this@MyAddressActivity )
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
                ViewController.customToast(applicationContext, "Mobile number and alternate mobile number both are same")
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

            val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")

            if (!ViewController.validateMobile(mobile)) {
                ViewController.customToast(applicationContext, "Enter valid mobile")
            }else if (!ViewController.validateMobile(alternateMobile)) {
                ViewController.customToast(applicationContext, "Enter valid alternate mobile")
            } else {
                val apiServices = RetrofitClient.apiInterface
                val call =
                    apiServices.updateAddressApi(
                        getString(R.string.api_key),
                        userId.toString(),
                        itemsData.id,
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
                                    if (!ViewController.noInterNetConnectivity(applicationContext)) {
                                        ViewController.showToast(applicationContext, "Please check your connection ")
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
        }
        bottomSheetDialog.show()
    }
    private fun getCityListUpadteApi(spinnerCity: Spinner, spinnerArea: Spinner) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCityListApi(getString(R.string.api_key) )
        call.enqueue(object : Callback<CityModel> {
            override fun onResponse(call: Call<CityModel>, response: Response<CityModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val stateList = response.body()?.response ?: emptyList()

                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@MyAddressActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.cityName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerCity.adapter = adapter

                            // Handle item selection
                            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                                    getAreaListUpdateApi(stateId, spinnerArea)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    // Handle when nothing is selected, if needed
                                }
                            }

                            if (!updateCityName.equals("")){
                                val position = stateList.indexOfFirst { it.cityName.equals(updateCityName, ignoreCase = true) }
                                if (position != -1) {
                                    // Set the default selection to the city found
                                    spinnerCity.setSelection(position)
                                    val selectedState = stateList[position]
                                    val stateId = selectedState.cityId
                                    cityName = selectedState.cityName

                                    areaName = ""
                                    // Call the function to update the areas
                                    getAreaListUpdateApi(stateId, spinnerArea)
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
    private fun getAreaListUpdateApi(stateId: String, spinnerArea: Spinner) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getAreaListApi(getString(R.string.api_key), stateId )
        call.enqueue(object : Callback<AreaModel> {
            override fun onResponse(call: Call<AreaModel>, response: Response<AreaModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status==true) {
                            val stateList = response.body()?.response ?: emptyList()
                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@MyAddressActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.areaName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerArea.adapter = adapter

                            // Optional: Handle item selection
                            spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

                            val position = stateList.indexOfFirst { it.areaName.equals(updateAreaName, ignoreCase = true) }
                            if (position != -1) {
                                // Set the default selection to the city found
                                spinnerArea.setSelection(position)
                                val selectedState = stateList[position]
                                val stateId = selectedState.areaId
                                areaName = selectedState.areaName
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

    //delete address
    private fun deleteAddressApi(addressId: String) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.deleteAddressApi(getString(R.string.api_key), addressId )
        call.enqueue(object : Callback<AddressModel> {
            override fun onResponse(call: Call<AddressModel>, response: Response<AddressModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status==true) {
                            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                                ViewController.showToast(applicationContext, "Please check your connection ")
                            } else {
                                addressListApi()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                }
            }
            override fun onFailure(call: Call<AddressModel>, t: Throwable) {
                Log.e("onFailureCategoryModel", "API Call Failed: ${t.message}")
            }
        })
    }

    //add address
    private fun addAddressDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@MyAddressActivity, R.style.AppBottomSheetDialogTheme)
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

            ViewController.hideKeyBoard(this@MyAddressActivity )
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
                ViewController.customToast(applicationContext, "Mobile number and alternate mobile number both are same")
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

            val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")

            if (!ViewController.validateMobile(mobile)) {
                ViewController.customToast(applicationContext, "Enter valid mobile")
            }else if (!ViewController.validateMobile(alternateMobile)) {
                ViewController.customToast(applicationContext, "Enter valid alternate mobile")
            } else {
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
                                    if (!ViewController.noInterNetConnectivity(applicationContext)) {
                                        ViewController.showToast(applicationContext, "Please check your connection ")
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
        }
        bottomSheetDialog.show()
    }
    private fun getCityListApi(spinnerCity: Spinner, spinnerArea: Spinner) {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCityListApi(getString(R.string.api_key) )
        call.enqueue(object : Callback<CityModel> {
            override fun onResponse(call: Call<CityModel>, response: Response<CityModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status==true) {
                            val stateList = response.body()?.response ?: emptyList()
                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@MyAddressActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.cityName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerCity.adapter = adapter

                            // Optional: Handle item selection
                            spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        val call = apiServices.getAreaListApi(getString(R.string.api_key), stateId )
        call.enqueue(object : Callback<AreaModel> {
            override fun onResponse(call: Call<AreaModel>, response: Response<AreaModel>) {
                try {
                    if (response.isSuccessful) {
                        if (response.body()?.status==true) {
                            val stateList = response.body()?.response ?: emptyList()
                            // Update the spinner
                            val adapter = ArrayAdapter(
                                this@MyAddressActivity,
                                android.R.layout.simple_spinner_item,
                                stateList.map { it.areaName }
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerArea.adapter = adapter

                            // Optional: Handle item selection
                            spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }


}