package com.test.mvvm.ui.auth

import `in`.aabhasjindal.otptextview.OtpTextView
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.mvvm.R
import com.test.mvvm.databinding.FragmentOtpBinding
import com.test.mvvm.utils.ConnectivityCheck
import com.test.mvvm.utils.SharedPrefs
import es.dmoral.toasty.Toasty
import java.util.*

class OtpFragment: Fragment() {

    private lateinit var binding: FragmentOtpBinding
    private lateinit var userPhone: String
    private lateinit var sharedPrefs: SharedPrefs
    private var connectivityCheck: ConnectivityCheck = ConnectivityCheck()
    private lateinit var otpTextView: OtpTextView
    private var START_TIME_MILLIS: Long = 300000
    private lateinit var countDownTimer: CountDownTimer
    private var mTimerRunning: Boolean = false
    private var timeLeftInMillis: Long = START_TIME_MILLIS

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtpBinding.inflate(layoutInflater)

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(requireContext())

        binding.otpProgressbar.visibility = View.INVISIBLE

        try {
            userPhone = sharedPrefs.read("userPhone_Key","").toString()
            binding.phoneNumberInVerificationPage.setText(userPhone)

        } catch (e: Exception){
            Log.i("Error", e.message.toString())
        }

        otpTextView = binding.otpView
        otpTextView.otpListener
        otpTextView.requestFocusOTP()
        otpTextView.resetState()

        startCountDown()

        binding.verifyOtp.setOnClickListener {
            binding.otpProgressbar.visibility = View.VISIBLE

            val finalOtp = otpTextView.otp

            if (finalOtp!!.length == 6) {
                if (connectivityCheck.checkInternet(requireActivity())) {
                    binding.verifyOtp.isEnabled = false

                } else {
                    Toasty.error(
                        requireActivity(),
                        "No Internet",
                        Toasty.LENGTH_LONG
                    ).show()
                }

            } else {
                binding.otpProgressbar.visibility = View.INVISIBLE
                Toasty.error(
                    requireActivity(),
                    resources.getString(R.string.invalid_otp),
                    Toasty.LENGTH_LONG
                ).show()
            }
        }

        binding.resendOtp.setOnClickListener {
            resetCountDown()
        }

        return binding.root
    }

    private fun startCountDown() {
        Log.i("countStart", "count testing")
        countDownTimer =  object: CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                mTimerRunning = false
            }
        }.start()

        mTimerRunning = true
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

        binding.tvCountdown.text = timeLeftFormatted
    }

    private fun resetCountDown() {
        countDownTimer.cancel()
        mTimerRunning = false

        Log.i("countReset", "reset testing")

        timeLeftInMillis = START_TIME_MILLIS
        updateCountDownText()
    }
}
