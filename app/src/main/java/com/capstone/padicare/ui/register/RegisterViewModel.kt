package com.capstone.padicare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.RegisterResponse
import com.capstone.padicare.helper.ResultState
import kotlinx.coroutines.launch

class RegisterViewModel (private val userRepo: UserRepository): ViewModel() {
    private val _registrationResult = MutableLiveData<ResultState<RegisterResponse>>()
    val registrationResult: LiveData<ResultState<RegisterResponse>> get() = _registrationResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _registrationResult.value = ResultState.Loading
                val result = userRepo.register(name, email, password)
                _registrationResult.postValue(result)
            } catch (e: Exception) {
                _registrationResult.postValue(ResultState.Error("${e.message}"))
            }
        }
    }
}