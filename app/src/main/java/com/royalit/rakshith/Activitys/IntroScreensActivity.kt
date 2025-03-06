package com.royalit.rakshith.Activitys

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.royalit.rakshith.Adapters.IntroSliderAdapter
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Logins.LoginActivity
import com.royalit.rakshith.Models.IntroSlide
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityIntroScreensBinding
import com.royalit.rakshith.databinding.ActivitySplashBinding

class IntroScreensActivity : AppCompatActivity() {

    val binding: ActivityIntroScreensBinding by lazy {
        ActivityIntroScreensBinding.inflate(layoutInflater)
    }

    private val introSliderAdapter = IntroSliderAdapter(
        listOf(
            IntroSlide(
                "Vegetables at Your Fingertips",
                "Get fresh and organic vegetables delivered to your doorstep with ease. Shop locally and enjoy healthy eating every day!",
                R.drawable.logo1
            ),
            IntroSlide(
                "Fresh Delivered, Hassle Free",
                "Get fresh vegetables delivered quickly and effortlessly to your doorstep.",
                R.drawable.logo2
            ),
            IntroSlide(
                "Shop Smart, Eat Fresh",
                "making smart choices when shopping for food, focusing on fresh, healthy ingredients that are good for you and your budget.",
                R.drawable.logo3
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        inIts()


    }


    private fun inIts() {
        binding.introSliderViewPager.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)

        binding.introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)

                // Change button text dynamically
                if (position == introSliderAdapter.itemCount - 1) {
                    binding.txtNext.text = "Finish"
                } else {
                    binding.txtNext.text = "Next"
                }
            }
        })

        binding.txtNext.setOnClickListener {
            if (binding.introSliderViewPager.currentItem + 1 < introSliderAdapter.itemCount) {
                binding.introSliderViewPager.currentItem += 1
            } else {
                Intent(applicationContext, LoginActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }

        binding.txtSkip.setOnClickListener {
            Intent(applicationContext, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }



    private fun setupIndicators() {
        binding.indicatorContainer.removeAllViews() // Clear previous indicators

        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            setMargins(8, 0, 8, 0)
        }

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
                this.layoutParams = layoutParams
            }

            binding.indicatorContainer.addView(indicators[i])
        }
    }


    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainer.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

}