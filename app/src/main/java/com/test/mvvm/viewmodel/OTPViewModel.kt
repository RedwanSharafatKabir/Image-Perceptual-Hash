package com.test.mvvm.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.mvvm.data.requests.OtpRequest
import com.test.mvvm.data.response.OTPResponse
import com.test.mvvm.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OTPViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository: MainRepository = MainRepository(application)
    val testResponse = MutableLiveData<OTPResponse>()
    val errorMessage = MutableLiveData<String>()

    fun testFunction(otpRequest: OtpRequest) = viewModelScope.launch {
        try{
            val call = appRepository.testFunction(otpRequest)

            call.enqueue(object: Callback<OTPResponse> {
                override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                    testResponse.postValue(response.body())
                }

                override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                    errorMessage.postValue(t.message)
                }
            })

        } catch (e: Exception){
            Log.i("http_exception", e.message.toString())
        }
    }
}
