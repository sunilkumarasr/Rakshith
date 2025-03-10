package com.royalit.rakshith.Config

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.internal.ContextUtils
import com.royalit.rakshith.R

object ViewController {

    var mProgressDialog: ProgressDialog? = null
    private var toast: Toast? = null

    fun changeStatusBarColor(activity: Activity, color: Int, isLight: Boolean) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = color
        WindowInsetsControllerCompat(activity.window, activity.window.decorView).isAppearanceLightStatusBars = isLight
    }

    @SuppressLint("RestrictedApi")
    fun removeStatusBar(context: Context) {
        val window = ContextUtils.getActivity(context)?.window
        window?.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor =
                ContextCompat.getColor(context, android.R.color.transparent)
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun customToast(context: Context, message: String) {
        if (toast == null) {
            toast = Toast(context.applicationContext)
            toast?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.toastlayout, null)

            val textView = view.findViewById<TextView>(R.id.txtNote)
            textView.text = message

            toast?.view = view
            toast?.duration = Toast.LENGTH_LONG

            // Apply animations
            val window = toast?.view?.windowToken
            if (window != null) {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val params = WindowManager.LayoutParams()
                params.windowAnimations = R.style.CustomToastAnimation
                wm.updateViewLayout(toast?.view, params)
            }
        } else {
            val textView = toast?.view?.findViewById<TextView>(R.id.txtNote)
            textView?.text = message
        }
        toast?.show()
    }


    fun customToastBottom(context: Context, message: String) {
        if (toast == null) {
            toast = Toast(context.applicationContext)
            toast?.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)

            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.toastlayout, null)

            val textView = view.findViewById<TextView>(R.id.txtNote)
            textView.text = message

            toast?.view = view
            toast?.duration = Toast.LENGTH_LONG

            // Apply animations
            val window = toast?.view?.windowToken
            if (window != null) {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val params = WindowManager.LayoutParams()
                params.windowAnimations = R.style.CustomToastAnimation
                wm.updateViewLayout(toast?.view, params)
            }
        } else {
            val textView = toast?.view?.findViewById<TextView>(R.id.txtNote)
            textView?.text = message
        }
        toast?.show()
    }

    fun showLoading(context: Context) {
        mProgressDialog = ProgressDialog(context).apply {
            setMessage("Loading...")
            show()
        }
    }
    fun hideLoading() {
        mProgressDialog?.dismiss()
    }


    fun noInterNetConnectivity(ctx: Context):Boolean
    {
        val connectivityManager= ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    fun hideKeyBoard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun validateEmail(email: String): Boolean {
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern))
    }
    fun validateMobile(phone: String): Boolean {
        val regex = Regex("^[6-9][0-9]{9}$")
        return phone.matches(regex)
    }

    fun hasEditText(editText: EditText, errMsg: String??
    ): Boolean {
        if (editText.text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = errMsg
            editText.requestFocus()//(editText, context)
            return false
        } else {
            // editText.isFocusable= false
        }
        return true
    }


    fun animation(): AnimationSet {
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            interpolator = DecelerateInterpolator()
            duration = 50
        }
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            interpolator = AccelerateInterpolator()
            startOffset = 100
            duration = 100
        }
        return AnimationSet(false).apply {
            addAnimation(fadeIn)
            // Uncomment the line below if you want to add fadeOut to the animation sequence
//             addAnimation(fadeOut)
        }
    }

    private fun animateNumberScrollTopToBottom(textView: TextView, oldValue: Int, newValue: Int) {
        val animator = ValueAnimator.ofInt(oldValue, newValue)
        animator.duration = 300 // Number change duration

        // Update number during animation
        animator.addUpdateListener { animation ->
            textView.text = (animation.animatedValue as Int).toString()
        }

        // Move text from the top (-100px) to the center (0px)
        val moveDown = ObjectAnimator.ofFloat(textView, "translationY", -60f, 0f)
        moveDown.duration = 300
        moveDown.interpolator = DecelerateInterpolator()

        animator.start()
        moveDown.start()
    }

    private fun animateNumberScrollBottomToTop(textView: TextView, oldValue: Int, newValue: Int) {
        val animator = ValueAnimator.ofInt(oldValue, newValue)
        animator.duration = 300 // Number change duration

        // Update number during animation
        animator.addUpdateListener { animation ->
            textView.text = (animation.animatedValue as Int).toString()
        }

        // Move text from the top (-100px) to the center (0px)
        val moveDown = ObjectAnimator.ofFloat(textView, "translationY", +60f, 0f)
        moveDown.duration = 300
        moveDown.interpolator = DecelerateInterpolator()

        animator.start()
        moveDown.start()
    }



}