package com.capstone.padicare.data.retrofit

import android.content.Context
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.data.pref.dataStore
import com.capstone.padicare.data.repo.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}