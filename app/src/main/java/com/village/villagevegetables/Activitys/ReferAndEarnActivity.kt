package com.village.villagevegetables.Activitys

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityReferAndEarnBinding

class ReferAndEarnActivity : AppCompatActivity() {

    val binding: ActivityReferAndEarnBinding by lazy {
        ActivityReferAndEarnBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            ReferAndEarnApp()
        }

        val userId = Preferences.loadStringValue(this@ReferAndEarnActivity, Preferences.userId, "")
        binding.txtUserId.text = "VV"+userId

        binding.linearURL.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearURL.startAnimation(animations)
            val appPackageName = packageName
            val appLink = "https://play.google.com/store/apps/details?id=$appPackageName"
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("VV"+userId, appLink)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "copied to clipboard!", Toast.LENGTH_SHORT).show()
        }


    }

    private fun ReferAndEarnApp() {
        // Replace with your app's package name
        val appPackageName = packageName
        val appLink = "https://play.google.com/store/apps/details?id=$appPackageName"
        // Create the intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "ReferID")
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this app: $appLink")
        }
        // Launch the share chooser
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}