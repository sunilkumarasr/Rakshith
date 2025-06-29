package com.village.villagevegetables.Activitys

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivitySplashBinding
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    lateinit var token: String


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.white),
            false
        )

        askNotificationPermission()

    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                inIts()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }else{
            inIts()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            askNotificationPermission()
        } else {
            Toast.makeText(
                this, "${getString(R.string.app_name)} can't post notifications without Notification permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun inIts() {

        LogoAnimation()
        
        //language
        val languageCode = Preferences.loadStringValue(applicationContext, Preferences.languageCode, "")
        if (languageCode != null) {
            setLocale(languageCode)
        }

        val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")
        Handler(Looper.getMainLooper()).postDelayed({
            if (loginCheck.equals("Open")) {
                val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            } else {
                val intent = Intent(this@SplashActivity, IntroScreensActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }, 2000)
    }

    private fun LogoAnimation() {
        val splashLogo: LinearLayout = findViewById(R.id.imgLogo)
        // Create ObjectAnimators for the different effects
        val scaleX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 0f, 1f)
        val fadeIn = ObjectAnimator.ofFloat(splashLogo, "alpha", 0f, 1f)
        val moveUp = ObjectAnimator.ofFloat(splashLogo, "translationY", 1000f, 0f) // Move logo up

        // Set durations for animations
        scaleX.duration = 1000
        scaleY.duration = 1000
        fadeIn.duration = 1000
        moveUp.duration = 1000

        // Combine animations in AnimatorSet to run together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, fadeIn, moveUp)

        // Start animation
        animatorSet.start()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onResume() {
        super.onResume()
        inIts()
    }

}