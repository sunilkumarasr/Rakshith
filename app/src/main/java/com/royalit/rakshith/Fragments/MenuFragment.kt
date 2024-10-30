package com.royalit.rakshith.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.royalit.rakshith.Activitys.AboutUsActivity
import com.royalit.rakshith.Activitys.AddAddressActivity
import com.royalit.rakshith.Activitys.CartActivity
import com.royalit.rakshith.Activitys.DashBoardActivity
import com.royalit.rakshith.Activitys.EditAddressActivity
import com.royalit.rakshith.Activitys.EditProfileActivity
import com.royalit.rakshith.Activitys.MyOrdersActivity
import com.royalit.rakshith.Activitys.PrivacyPolicyActivity
import com.royalit.rakshith.Activitys.TermsAndConditionsActivity
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.ActivityAddAddressBinding
import com.royalit.rakshith.databinding.FragmentMenuBinding

class MenuFragment : Fragment() ,View.OnClickListener{

    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {

        }

        binding.linearProfile.setOnClickListener(this)
        binding.linearCart.setOnClickListener(this)
        binding.linearMyAddress.setOnClickListener(this)
        binding.linearOrder.setOnClickListener(this)
        binding.linearLanguage.setOnClickListener(this)
        binding.linearCoupon.setOnClickListener(this)
        binding.linearMyWallet.setOnClickListener(this)
        binding.linearReferAndEarn.setOnClickListener(this)
        binding.linearHelpAndSupport.setOnClickListener(this)
        binding.linearAbout.setOnClickListener(this)
        binding.linearTermsAndConditions.setOnClickListener(this)
        binding.linearPrivacyPolicy.setOnClickListener(this)
        binding.linearRefundPolicy.setOnClickListener(this)
        binding.linearShippingPolicy.setOnClickListener(this)
        binding.linearLogout.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linearProfile -> {
                val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearCart -> {
                val intent = Intent(requireActivity(), CartActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearMyAddress -> {
                val intent = Intent(requireActivity(), AddAddressActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearOrder -> {
                val intent = Intent(requireActivity(), MyOrdersActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearLanguage -> {

            }
            R.id.linearCoupon -> {

            }
            R.id.linearMyWallet -> {

            }
            R.id.linearReferAndEarn -> {

            }
            R.id.linearHelpAndSupport -> {

            }
            R.id.linearAbout -> {
                val intent = Intent(requireActivity(), AboutUsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearTermsAndConditions -> {
                val intent = Intent(requireActivity(), TermsAndConditionsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearPrivacyPolicy -> {
                val intent = Intent(requireActivity(), PrivacyPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearRefundPolicy -> {

            }
            R.id.linearShippingPolicy -> {

            }
            R.id.linearLogout -> {
                LogoutDialog()
            }
        }
    }


    private fun LogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheetDialog.setContentView(view)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

}