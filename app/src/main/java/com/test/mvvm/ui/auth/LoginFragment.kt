package com.test.mvvm.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.test.mvvm.databinding.FragmentLoginBinding
import com.test.mvvm.R
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.test.mvvm.data.requests.OtpRequest
import com.test.mvvm.utils.ConnectivityCheck
import com.test.mvvm.utils.SharedPrefs
import com.test.mvvm.viewmodel.OTPViewModel
import es.dmoral.toasty.Toasty

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPrefs: SharedPrefs
    private lateinit var otpViewModel: OTPViewModel
    private var connectivityCheck: ConnectivityCheck = ConnectivityCheck()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        otpViewModel = ViewModelProvider(this)[OTPViewModel::class.java]

        sharedPrefs = SharedPrefs()
        sharedPrefs.init(requireContext())

        // Button click listener
        binding.buttonConfirm.setOnClickListener {
            val userPhone: String = binding.enterUserPhone.text.toString()

            if (userPhone.isNotEmpty() && userPhone.length == 11) {
                sharedPrefs.write("userPhone_Key", userPhone)

                if (connectivityCheck.checkInternet(requireActivity())) {
                    generateOtp(userPhone)

                    Navigation.createNavigateOnClickListener(R.id.otpFragment)
                        .onClick(binding.buttonConfirm)
                    binding.enterUserPhone.setText("")

                } else {
                    Toasty.error(
                        requireActivity(),
                        "No Internet",
                        Toasty.LENGTH_LONG
                    ).show()
                }

            } else {
                binding.enterUserPhone.error = "Enter valid phone number"
            }
        }

        return binding.root
    }

    private fun generateOtp(userPhone: String) {
        otpViewModel.testFunction(OtpRequest(userPhone))

        otpViewModel.testResponse.observe(viewLifecycleOwner) {
            val value = it
            Log.i("response_value", "$value")
        }

        otpViewModel.errorMessage.observe(viewLifecycleOwner) {
            Log.i("error_message", it)
        }
    }
}
