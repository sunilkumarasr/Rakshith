package com.village.villagevegetables.Activitys

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.village.villagevegetables.Activitys.AllProductsListActivity
import com.village.villagevegetables.Activitys.CheckOutActivity
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddAddressModel
import com.village.villagevegetables.Models.AreaModel
import com.village.villagevegetables.Models.CityModel
import com.village.villagevegetables.Models.SettingsModel
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityDashBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    //bottom menu
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    //exit
    private var isHomeFragmentDisplayed = false

    var cityId: String = ""
    var cityName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        inIts()

    }

    private fun inIts() {

        Preferences.saveStringValue(this@DashBoardActivity, Preferences.LOGINCHECK, "Open")

        val name = Preferences.loadStringValue(this@DashBoardActivity, Preferences.name, "")
        val cityName = Preferences.loadStringValue(this@DashBoardActivity, Preferences.cityName, "")
        binding.txtUserName.text = "Hi "+name
        binding.txtUserLocation.text = cityName
        if (cityName.equals("")){
            //locationPopup()
        }

        binding.linearLocationChange.setOnClickListener {
            //locationPopup()
        }

        //LottieAnimation color change
        binding.lotiNotification.addValueCallback(
            KeyPath("**"),  // Applies to all layers
            LottieProperty.COLOR_FILTER
        ) { PorterDuffColorFilter(ContextCompat.getColor(this@DashBoardActivity, R.color.white), PorterDuff.Mode.SRC_ATOP) }
        binding.root.findViewById<LottieAnimationView> (R.id.lotiCart).addValueCallback(
            KeyPath("**"),  // Applies to all layers
            LottieProperty.COLOR_FILTER
        ) { PorterDuffColorFilter(ContextCompat.getColor(this@DashBoardActivity, R.color.white), PorterDuff.Mode.SRC_ATOP) }

        bottomMenu()

        binding.linearCart.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearCart.startAnimation(animations)
            val intent = Intent(this@DashBoardActivity, CartActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.lotiNotification.setOnClickListener {
            val animations = ViewController.animation()
            binding.lotiNotification.startAnimation(animations)
            val intent = Intent(this@DashBoardActivity, NotificationsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.linearSearch.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSearch.startAnimation(animations)
            val intent = Intent(this@DashBoardActivity, SearchActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            settingsApi()
            getCartApi()
        }

    }

    private fun locationPopup() {
        val bottomSheetDialog = BottomSheetDialog(this@DashBoardActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_location, null)
        bottomSheetDialog.setContentView(view)

        val city = Preferences.loadStringValue(this@DashBoardActivity, Preferences.cityName, "")
        if (city.equals("")){
            // Disable touch outside to dismiss dialog
            bottomSheetDialog.setCancelable(false)
            bottomSheetDialog.setCanceledOnTouchOutside(false)
        }

        val spinnerCity = view.findViewById<Spinner>(R.id.spinnerCity)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            getCityListApi(spinnerCity)
        }


        linearSubmit.setOnClickListener {
            if (!cityName.equals("")){
                Preferences.saveStringValue(this@DashBoardActivity, Preferences.cityId, cityId)
                Preferences.saveStringValue(this@DashBoardActivity, Preferences.cityName, cityName)
                binding.txtUserLocation.text = cityName
                bottomSheetDialog.dismiss()
            }else{
                ViewController.customToast(applicationContext, "select your address")
            }
        }

        bottomSheetDialog.show()
    }
    private fun getCityListApi(spinnerCity: Spinner) {
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
                                this@DashBoardActivity,
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
                                    cityId = selectedState.cityId
                                    cityName = selectedState.cityName
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

    private fun bottomMenu() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.frame_layout)
        // Setup Bottom Navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.home -> {
                    binding.linearSearch.visibility = View.VISIBLE
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.favorite -> {
                    binding.linearSearch.visibility = View.GONE
                    navController.navigate(R.id.favouriteFragment)
                    true
                }

                R.id.orders -> {
                    binding.linearSearch.visibility = View.GONE
                    navController.navigate(R.id.ordersFragment)
                    true
                }

                R.id.menu -> {
                    binding.linearSearch.visibility = View.GONE
                    navController.navigate(R.id.menuFragment)
                    true
                }

                else -> false
            }
        }
    }

    fun settingsApi() {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.settingsApi(
            getString(R.string.api_key)
        )
        call.enqueue(object : Callback<SettingsModel> {
            override fun onResponse(
                call: Call<SettingsModel>,
                response: Response<SettingsModel>
            ) {
                try {
                    if (response.isSuccessful) {
                        Preferences.saveStringValue(this@DashBoardActivity, Preferences.minAmount,response.body()?.response!!.get(0).cartText.toString())
                        if (!response.body()?.response!!.get(0).appMode.equals("online")){
                            offlineAppPopup()
                        }

                        if (!response.body()?.response!!.get(0).version.equals("12")){
                            upDateAppPopup()
                        }



                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }

            override fun onFailure(call: Call<SettingsModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
            }
        })
    }
    private fun offlineAppPopup() {
        val bottomSheetDialog = BottomSheetDialog(this@DashBoardActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_offline, null)
        bottomSheetDialog.setContentView(view)

        // Disable touch outside to dismiss dialog
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        bottomSheetDialog.show()
    }
    private fun upDateAppPopup() {
        val bottomSheetDialog = BottomSheetDialog(this@DashBoardActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_updateapp, null)
        bottomSheetDialog.setContentView(view)

        // Disable touch outside to dismiss dialog
        bottomSheetDialog.setCancelable(false)
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        val cardUpdate = view.findViewById<CardView>(R.id.cardUpdate)
        cardUpdate.setOnClickListener {
            // Redirect to Play Store or start update logic
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.village.villagevegetables"))
            startActivity(intent)
//            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }


    fun getCartApi() {
        val userId = Preferences.loadStringValue(this@DashBoardActivity, Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getCartApi(
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
                        binding.root.findViewById<TextView>(R.id.cart_badge_count).text = response.body()?.ResponseCartList!!.size.toString()
                        Preferences.saveStringValue(this@DashBoardActivity, Preferences.cartCount, response.body()?.ResponseCartList!!.size.toString())
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

     fun cartCount(count: String, cartStatus: String) {
         Log.e("cartStatus_",cartStatus)
         if (cartStatus.equals("1")){
             getCartApi()
         }else{
             binding.root.findViewById<TextView>(R.id.cart_badge_count).text = count.toString()
             Preferences.saveStringValue(this@DashBoardActivity, Preferences.cartCount, count)
         }
     }

    override fun onBackPressed() {
        if (isHomeFragmentDisplayed) {
            exitDialog()
        } else {
            isHomeFragmentDisplayed = true
            // Navigate to HomeFragment
            navigateToHomeFragment()
        }
    }
    private fun navigateToHomeFragment() {
        binding.linearSearch.visibility = View.VISIBLE
        navController.navigate(R.id.homeFragment)
        bottomNavigationView.selectedItemId = R.id.home
    }

    private fun exitDialog() {
        isHomeFragmentDisplayed = false
        val dialogBuilder = AlertDialog.Builder(this@DashBoardActivity)
        dialogBuilder.setTitle(R.string.Exit)
        dialogBuilder.setMessage(R.string.Areyousurewanttoexitthisapp)
        dialogBuilder.setPositiveButton(R.string.ok) { dialog, _ ->
            finishAffinity()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        val b = dialogBuilder.create()
        b.show()
    }


}