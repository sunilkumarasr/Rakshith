package com.royalit.rakshith.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary), false)

        inits()

    }

    private fun inits() {
        bottomMenu()
    }

    private fun bottomMenu() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.frame_layout)

        // Setup Bottom Navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Set listener for item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.favorite -> {
                    navController.navigate(R.id.favouriteFragment)
                    true
                }

                R.id.cart -> {
                    navController.navigate(R.id.cartFragment)
                    true
                }

                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitDialog()
    }

    private fun exitDialog(){
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