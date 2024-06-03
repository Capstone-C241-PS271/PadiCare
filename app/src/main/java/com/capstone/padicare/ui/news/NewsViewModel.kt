package com.capstone.padicare.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.padicare.data.pref.UserModel
import com.capstone.padicare.data.repo.NewsRepository
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.response.NewsItem
import com.capstone.padicare.response.NewsResponse

class NewsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val newsRepository = NewsRepository()
    private val _newsList = MutableLiveData<List<NewsItem>>()
    val newsList: LiveData<List<NewsItem>> = _newsList

    fun fetchNews() {
        newsRepository.seacrhNews(
            onSucess = { articleList ->
                val newsItemList = articleList.map {
                    val description = it.description ?: ""
                    val imageUrl = it.urlToImage ?: ""
                    NewsItem(it.title, imageUrl, it.url, description)
                }
                _newsList.postValue(newsItemList)
            },
            onFailure = {

            }
        )
    }

    fun getData() : LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}