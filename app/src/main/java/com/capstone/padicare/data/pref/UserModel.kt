package com.capstone.padicare.data.pref

data class UserModel (
    val userId: String,
    val name: String,
    val email: String,
    val token: String,
    var isLogin: Boolean = false
)