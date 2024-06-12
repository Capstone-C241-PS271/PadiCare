package com.capstone.padicare.data.retrofit

import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.response.BaseResponse
import com.capstone.padicare.data.response.Data
import com.capstone.padicare.data.response.LoginRequest
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.data.response.PredictRequest
import com.capstone.padicare.data.response.PredictResponse
import com.capstone.padicare.data.response.RegisterRequest
import com.capstone.padicare.data.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header


interface ApiService {

    @POST("/api/users/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @POST("/api/users/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("/api/users/me")
    suspend fun getUserInfo(@Header("Authorization") token: String): Response<BaseResponse<UserModel>>

    @POST("/api/predictions/")
    suspend fun predict(@Header("Authorization") token: String, @Body predictRequest: PredictRequest): Response<PredictResponse>

    @GET("/api/predictions/")
    suspend fun getHistory(@Header("Authorization") token: String): Response<BaseResponse<List<Data>>>

}