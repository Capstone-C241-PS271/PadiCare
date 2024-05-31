package com.capstone.padicare.data.retrofit

import android.content.Context
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.data.pref.dataStore
import com.capstone.padicare.data.repo.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService)
    }
}