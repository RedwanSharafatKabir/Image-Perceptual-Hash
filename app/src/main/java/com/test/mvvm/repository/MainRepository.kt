package com.test.mvvm.repository

import android.content.Context
import com.test.mvvm.data.requests.OtpRequest
import com.test.mvvm.retrofit.APIService
import com.test.mvvm.retrofit.APIConfig

class MainRepository(ctx: Context) {

    private val apiService:APIService = APIConfig().create(ctx)

    suspend fun testFunction(otpRequest: OtpRequest) = apiService.testFunction(otpRequest)
}
