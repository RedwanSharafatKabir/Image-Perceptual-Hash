package com.test.mvvm.retrofit

import com.test.mvvm.data.EndPoints
import com.test.mvvm.data.requests.OtpRequest
import com.test.mvvm.data.response.OTPResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @Headers("Content-Type: application/json")
    @POST(EndPoints.SEND_OTP)
    suspend fun testFunction(@Body otpRequest: OtpRequest): Call<OTPResponse>
}
