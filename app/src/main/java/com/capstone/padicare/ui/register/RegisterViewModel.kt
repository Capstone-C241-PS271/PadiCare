package com.capstone.padicare.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.RegisterResponse
import com.capstone.padicare.helper.ResultState
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registerResult = MutableLiveData<ResultState<RegisterResponse>>()
    val registerResult: LiveData<ResultState<RegisterResponse>> = _registerResult

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = ResultState.Loading

            val result = userRepository.register(name, email, password)
            _registerResult.value = result
        }
    }
}
