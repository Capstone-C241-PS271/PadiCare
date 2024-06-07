package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    val token: String
)

data class LoginRequest(
    val username: String,
    val password: String
)