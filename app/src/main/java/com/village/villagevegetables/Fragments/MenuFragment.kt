package com.village.villagevegetables.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.village.villagevegetables.Activitys.AboutUsActivity
import com.village.villagevegetables.Activitys.MyAddressActivity
import com.village.villagevegetables.Activitys.CartActivity
import com.village.villagevegetables.Activitys.DashBoardActivity
import com.village.villagevegetables.Activitys.DeleteAccountActivity
import com.village.villagevegetables.Activitys.EditProfileActivity
import com.village.villagevegetables.Activitys.FaqActivity
import com.village.villagevegetables.Activitys.MyOrdersActivity
import com.village.villagevegetables.Activitys.MyWalletActivity
import com.village.villagevegetables.Activitys.PrivacyPolicyActivity
import com.village.villagevegetables.Activitys.ReferAndEarnActivity
import com.village.villagevegetables.Activitys.RefundPolicyActivity
import com.village.villagevegetables.Activitys.ShippingPolicyActivity
import com.village.villagevegetables.Activitys.TermsAndConditionsActivity
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Logins.LoginActivity
import com.village.villagevegetables.R
import com.village.villagevegetables.databinding.FragmentMenuBinding
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

        val packageName = "com.village.villagevegetables"
        try {
            val packageInfo = requireActivity().packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            binding.txtVersion.text = "v$versionName"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        binding.linearProfile.setOnClickListener(this)
        binding.linearCart.setOnClickListener(this)
        binding.linearMyAddress.setOnClickListener(this)
        binding.linearOrder.setOnClickListener(this)
        binding.linearLanguage.setOnClickListener(this)
        binding.linearMyWallet.setOnClickListener(this)
        binding.linearReferAndEarn.setOnClickListener(this)
        binding.linearShare.setOnClickListener(this)
        binding.linearAbout.setOnClickListener(this)
        binding.linearTermsAndConditions.setOnClickListener(this)
        binding.linearPrivacyPolicy.setOnClickListener(this)
        binding.linearFaq.setOnClickListener(this)
        binding.linearRefundPolicy.setOnClickListener(this)
        binding.linearShippingPolicy.setOnClickListener(this)
        binding.linearDeleteAccount.setOnClickListener(this)
        binding.linearLogout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linearProfile -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)

                val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearCart -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), CartActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearMyAddress -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), MyAddressActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearOrder -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), MyOrdersActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearLanguage -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                LanguageDialog()
            }
            R.id.linearMyWallet -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), MyWalletActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearShare -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                shareApp()
            }
            R.id.linearReferAndEarn -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), ReferAndEarnActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearFaq -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), FaqActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearAbout -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), AboutUsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearTermsAndConditions -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), TermsAndConditionsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearPrivacyPolicy -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), PrivacyPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearRefundPolicy -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), RefundPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearShippingPolicy -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), ShippingPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearDeleteAccount -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                deleteAccountDialog()
            }
            R.id.linearLogout -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                LogoutDialog()
            }
        }
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

        val radioGroupLanguage = view.findViewById<RadioGroup>(R.id.radioGroupLanguage)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            val selectedRadioButtonId: Int = radioGroupLanguage.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton: RadioButton = view.findViewById(selectedRadioButtonId)
                val selectedLanguage = when (selectedRadioButton.text.toString()) {
                    "English" -> "en"
                    "Telugu" -> "te"
                    else -> "en"
                }

                // Save selected language and apply changes
                setLocale(selectedLanguage)
            }
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.show()
    }

    private fun setLocale(languageCode: String) {
        // Save the language preference
        Preferences.saveStringValue(requireActivity(), Preferences.languageCode, languageCode)

        // Set the new locale
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        requireActivity().baseContext.resources.updateConfiguration(
            config,
            requireActivity().baseContext.resources.displayMetrics
        )

        // Restart the activity to apply changes
        val intent = Intent(requireActivity(), DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun LogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheetDialog.setContentView(view)

        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
            Preferences.deleteSharedPreferences(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        bottomSheetDialog.show()

    }

    private fun deleteAccountDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_delete_account, null)
        bottomSheetDialog.setContentView(view)

        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
            val intent = Intent(requireActivity(), DeleteAccountActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        bottomSheetDialog.show()

    }

}