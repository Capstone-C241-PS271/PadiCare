package com.capstone.padicare.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.response.PostResponse
import kotlinx.coroutines.launch
import okio.IOException

class FeedViewModel(private val repository: UserRepository) : ViewModel() {
    private val _feed = MutableLiveData<Result<PostResponse>>()
    val feedResult : LiveData<Result<PostResponse>> = _feed

    fun createPost(token: String, postResponse: PostResponse) {
        viewModelScope.launch {
            try {
                val response = repository.createPost(token, postResponse)
                if (response.isSuccessful) {
                    _feed.value = Result.success(response.body()!!)
                } else {
                    _feed.value = Result.failure(IOException("Failed to create post"))
                }
            } catch (e: IOException) {
                _feed.value = Result.failure(e)
            } catch (e: Exception) {
                _feed.value = Result.failure(e)
            }
        }
    }
}