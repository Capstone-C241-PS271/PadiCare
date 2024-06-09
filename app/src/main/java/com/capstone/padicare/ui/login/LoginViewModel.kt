package com.capstone.padicare.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.helper.ResultState
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepo: UserRepository): ViewModel() {

    private val _loginResponse = MutableLiveData<ResultState<LoginResponse>>()
    val loginResponse: LiveData<ResultState<LoginResponse>> get() = _loginResponse

    fun login(email: String, password: String) {
        _loginResponse.value = ResultState.Loading

        viewModelScope.launch {
            val result = userRepo.login(email, password)
            _loginResponse.value = result
        }

    }
}