package com.capstone.padicare.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.UserRepository

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getData() : LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

}