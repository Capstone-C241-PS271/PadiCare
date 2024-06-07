package com.capstone.padicare.data.retrofit

import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.BaseResponse
import com.capstone.padicare.data.response.LoginRequest
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.data.response.RegisterRequest
import com.capstone.padicare.data.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface ApiService {
    /**
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
    **/

    @POST("/api/users/register")
    fun registerUser(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @POST("/api/users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/api/users/me")
    suspend fun getUserInfo(@Header("Authorization") token: String): Call<BaseResponse<UserModel>>

}