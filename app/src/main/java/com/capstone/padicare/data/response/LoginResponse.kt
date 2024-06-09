package com.capstone.padicare.data.response



data class LoginResponse (
    val token: String
)

data class LoginRequest(
    val username: String,
    val password: String
)