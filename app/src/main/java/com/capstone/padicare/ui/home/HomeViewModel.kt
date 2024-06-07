package com.capstone.padicare.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.BaseResponse
import com.capstone.padicare.helper.ResultState
import kotlinx.coroutines.launch

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userInfo = MutableLiveData<ResultState<BaseResponse<UserModel>>>()
    val userInfo: LiveData<ResultState<BaseResponse<UserModel>>> = _userInfo

    fun fetchUserInfo(token: String): LiveData<ResultState<BaseResponse<UserModel>>> {
        viewModelScope.launch {
            _userInfo.value = ResultState.Loading
            val result = userRepository.getUserInfo(token)
            _userInfo.value = result
        }
        return _userInfo
    }

}