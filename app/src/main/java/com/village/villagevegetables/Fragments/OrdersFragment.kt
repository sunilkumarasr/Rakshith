package com.village.villagevegetables.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.village.villagevegetables.Adapters.ViewPagerAdapter
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        // Set up the adapter
        binding.viewPager.adapter = ViewPagerAdapter(requireActivity())

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

}