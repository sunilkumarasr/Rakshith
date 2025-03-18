package com.village.villagevegetables.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.village.villagevegetables.Adapters.ViewPagerAdapter
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.ActivityMyOrdersBinding

class MyOrdersActivity : AppCompatActivity() {

    val binding: ActivityMyOrdersBinding by lazy {
        ActivityMyOrdersBinding.inflate(layoutInflater)
    }

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

        // Set up the adapter
        binding.viewPager.adapter = ViewPagerAdapter(this@MyOrdersActivity)

        // Link the TabLayout and ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_all)
                1 -> getString(R.string.tab_upcoming)
                2 -> getString(R.string.tab_complete)
                else -> getString(R.string.tab_all)
            }
        }.attach()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}