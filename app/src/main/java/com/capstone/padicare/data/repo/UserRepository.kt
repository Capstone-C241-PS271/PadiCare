package com.capstone.padicare.data.repo

import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.data.response.BaseResponse
import com.capstone.padicare.data.response.ErrorResponse
import com.capstone.padicare.data.response.LoginRequest
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.data.response.RegisterRequest
import com.capstone.padicare.data.response.RegisterResponse
import com.capstone.padicare.data.retrofit.ApiService
import com.capstone.padicare.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserRepository private constructor(
    private val userPref: UserPreference,
    private val apiService: ApiService) {

    suspend fun saveSession(user: UserModel){
        userPref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }

    suspend fun logout(){
        userPref.logout()
    }

    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse> {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResultState.Error("Please fill all fields")
        }

        val registerRequest = RegisterRequest(name, email, password)

        return suspendCancellableCoroutine { continuation ->
            val call = apiService.registerUser(registerRequest)
            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            continuation.resume(ResultState.Success(responseBody))
                        } else {
                            continuation.resume(ResultState.Error("Registration failed"))
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Registration failed"
                        continuation.resume(ResultState.Error(errorMessage))
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
            continuation.invokeOnCancellation {
                call.cancel()
            }
        }
    }

    suspend fun login(username: String, password: String): ResultState<LoginResponse> {
        return try {
            val response = apiService.loginUser(LoginRequest(username, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    ResultState.Success(loginResponse)
                } else {
                    ResultState.Error("Response body is null")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown Error"
                ResultState.Error(errorMessage)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown Error"
            ResultState.Error(errorMessage)
        }
    }

    suspend fun getUserInfo(token: String): ResultState<BaseResponse<UserModel>> {
        return try {
            val response = apiService.getUserInfo("Bearer $token")
            if (response.isSuccessful) {
                val userModel = response.body()
                if (userModel != null) {
                    ResultState.Success(userModel)
                } else {
                    ResultState.Error("Response body is null")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown Error"
                ResultState.Error(errorMessage)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown Error"
            ResultState.Error(errorMessage)
        } catch (e: Exception) {
            ResultState.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun setAuth(user: UserModel) = userPref.saveSession(user)

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
