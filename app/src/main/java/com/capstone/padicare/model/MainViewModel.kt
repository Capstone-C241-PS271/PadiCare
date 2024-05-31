package com.capstone.padicare.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.UserRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repo: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _Message = MutableLiveData<String>()
    val  message: LiveData<String> get() = _Message

    fun getSession(): LiveData<UserModel> {
        return repo.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            repo.logout()
        }
    }
}