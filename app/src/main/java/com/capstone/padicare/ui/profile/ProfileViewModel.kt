package com.capstone.padicare.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getData() : LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
