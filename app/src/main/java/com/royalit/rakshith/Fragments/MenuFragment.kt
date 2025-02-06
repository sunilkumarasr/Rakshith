package com.royalit.rakshith.Fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.royalit.rakshith.Activitys.AboutUsActivity
import com.royalit.rakshith.Activitys.MyAddressActivity
import com.royalit.rakshith.Activitys.CartActivity
import com.royalit.rakshith.Activitys.CouponActivity
import com.royalit.rakshith.Activitys.DashBoardActivity
import com.royalit.rakshith.Activitys.EditProfileActivity
import com.royalit.rakshith.Activitys.HelpAndSupportActivity
import com.royalit.rakshith.Activitys.MyOrdersActivity
import com.royalit.rakshith.Activitys.MyWalletActivity
import com.royalit.rakshith.Activitys.PrivacyPolicyActivity
import com.royalit.rakshith.Activitys.ProductsDetailsActivity
import com.royalit.rakshith.Activitys.RefundPolicyActivity
import com.royalit.rakshith.Activitys.ShippingPolicyActivity
import com.royalit.rakshith.Activitys.TermsAndConditionsActivity
import com.royalit.rakshith.Config.Preferences
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.R
import com.royalit.rakshith.databinding.FragmentMenuBinding
import java.util.Locale

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
        binding.linearShare.setOnClickListener(this)
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
                val intent = Intent(requireActivity(), MyAddressActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearOrder -> {
                val intent = Intent(requireActivity(), MyOrdersActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearLanguage -> {
                LanguageDialog()
            }
            R.id.linearCoupon -> {
                val intent = Intent(requireActivity(), CouponActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearMyWallet -> {
                val intent = Intent(requireActivity(), MyWalletActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearShare -> {
                shareApp()
            }
            R.id.linearReferAndEarn -> {
                ReferAndEarnApp()
            }
            R.id.linearHelpAndSupport -> {
                val intent = Intent(requireActivity(), HelpAndSupportActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
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
                val intent = Intent(requireActivity(), RefundPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearShippingPolicy -> {
                val intent = Intent(requireActivity(), ShippingPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearLogout -> {
                LogoutDialog()
            }
        }
    }

    private fun ReferAndEarnApp() {
        // Replace with your app's package name
        val appPackageName = requireContext().packageName
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


    private fun shareApp() {
        // Replace with your app's package name
        val appPackageName = requireContext().packageName
        val appLink = "https://play.google.com/store/apps/details?id=$appPackageName"
        // Create the intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this app: $appLink")
        }
        // Launch the share chooser
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun LanguageDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_languagedialog, null)
        bottomSheetDialog.setContentView(view)
        val radioGroupLanguage = view.findViewById<RadioGroup>(R.id.radioGroupLanguage) // Corrected this line
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonOk.setOnClickListener {
            val selectedRadioButtonId: Int = radioGroupLanguage.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = view.findViewById(selectedRadioButtonId)
                val selectedLanguage = selectedRadioButton.text.toString()
                when (selectedLanguage) {
                    "English" -> setLocale("en")
                    "Telugu" -> setLocale("te")
                }
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }
    private fun setLocale(languageCode: String) {
        Preferences.saveStringValue(requireActivity(), Preferences.languageCode,
            languageCode.toString()
        )
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)

        val intent = Intent(requireActivity(), DashBoardActivity::class.java)
        startActivity(intent)
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