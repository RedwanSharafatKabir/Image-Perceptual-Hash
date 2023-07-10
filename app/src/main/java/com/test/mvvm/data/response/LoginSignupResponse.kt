package com.test.mvvm.data.response

data class LoginSignupResponse(
    val statusCode: Int,
    val success: Boolean,
    val msg: String,
    val token: String
)
