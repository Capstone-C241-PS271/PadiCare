package com.capstone.padicare.data.response

data class RegisterResponse(
    val id: String,
    val email: String,
    val name: String,
    val token: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)