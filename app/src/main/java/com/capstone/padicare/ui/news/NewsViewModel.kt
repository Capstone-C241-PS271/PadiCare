package com.capstone.padicare.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.NewsRepository
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.BaseResponse
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.response.NewsItem
import com.capstone.padicare.response.NewsResponse
import kotlinx.coroutines.launch

class NewsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val newsRepository = NewsRepository()
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    private val _userInfo = MutableLiveData<ResultState<BaseResponse<UserModel>>>()
    val userInfo: LiveData<ResultState<BaseResponse<UserModel>>> = _userInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchNews() {
        _isLoading.value = true
        newsRepository.seacrhNews(
            onSucess = { articleList ->
                val newsItemList = articleList.map {
                    val description = it.description ?: ""
                    val imageUrl = it.urlToImage ?: ""
                    NewsItem(it.title, imageUrl, it.url, description)
                }
                _newsList.postValue(newsItemList)
                _isLoading.postValue(false)
            },
            onFailure = {
                _isLoading.postValue(false)
            }
        )
    }



    fun fetchUserInfo(token: String): LiveData<ResultState<BaseResponse<UserModel>>> {
        viewModelScope.launch {
            _userInfo.value = ResultState.Loading
            val result = userRepository.getUserInfo(token)
            _userInfo.value = result
        }
        return _userInfo
    }

    fun getData(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}
