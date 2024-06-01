package com.capstone.padicare.data.repo

import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.data.response.ErrorResponse
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

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

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
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