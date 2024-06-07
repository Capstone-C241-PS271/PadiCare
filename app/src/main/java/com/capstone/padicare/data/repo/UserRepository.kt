package com.capstone.padicare.data.repo

import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.data.response.ErrorResponse
import com.capstone.padicare.data.response.LoginRequest
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.data.retrofit.ApiService
import com.capstone.padicare.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


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

    /**
    suspend fun register(name: String, email: String, password: String): ResultState<RegisterResponse> {
        ResultState.Loading
        return try {
            val response = apiService.register(name, email, password)
            if (response.error == true){
                ResultState.Error(response.message ?: "Unknown Error")
            } else {
                ResultState.Success(response)
            }
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            ResultState.Error(errorMessage.toString())
        }
    }
    **/

    suspend fun login(username: String, password: String): ResultState<LoginResponse> {
        try {
            val response = apiService.loginUser(LoginRequest(username, password))
            if (response.isSuccessful) {
                val loginResponse = response.body()
                return if (loginResponse != null) {
                    ResultState.Success(loginResponse)
                } else {
                    ResultState.Error("Response body is null")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown Error"
                return ResultState.Error(errorMessage)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown Error"
            return ResultState.Error(errorMessage)
        }
    }

    suspend fun getUserInfo(token: String): ResultState<UserModel> {
        return try {
            val response = apiService.getUserInfo("Bearer $token").execute()
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