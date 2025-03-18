package com.village.villagevegetables.Activitys

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.village.villagevegetables.Adapters.Cart.CartListResponse
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
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

        Preferences.saveStringValue(this@DashBoardActivity, Preferences.LOGINCHECK, "Login")

        //LottieAnimation color change
        binding.lotiNotification.addValueCallback(
            KeyPath("**"),  // Applies to all layers
            LottieProperty.COLOR_FILTER
        ) { PorterDuffColorFilter(ContextCompat.getColor(this@DashBoardActivity, R.color.white), PorterDuff.Mode.SRC_ATOP) }
        binding.root.findViewById<LottieAnimationView>(R.id.lotiCart).addValueCallback(
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

        getCartApi()
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
        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this app?")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            finishAffinity()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val b = dialogBuilder.create()
        b.show()
    }


}