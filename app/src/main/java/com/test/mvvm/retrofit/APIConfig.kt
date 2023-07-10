package com.test.mvvm.retrofit

import android.content.Context
import com.test.mvvm.R
import com.test.mvvm.utils.SharedPrefs
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIConfig {
    fun create(context: Context): APIService {
        val sharedPrefs = SharedPrefs()
        sharedPrefs.init(context)

        val token = sharedPrefs.read("userToken_Key", "").toString()

        val httpClient = OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.header(
                    "Authorization", "Bearer $token"
                )
                chain.proceed(requestBuilder.build())
            }

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(APIService::class.java)
    }
}
