package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse (
    val token: String
)