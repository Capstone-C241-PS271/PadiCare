package com.capstone.padicare.data.repo

import com.capstone.padicare.api.ApiClient
import com.capstone.padicare.api.ApiConfig
import com.capstone.padicare.response.Article
import com.capstone.padicare.response.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {

    fun seacrhNews(
        onSucess: (List<Article>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val query = "agriculture OR farming OR agronomy OR crop OR livestock"
        ApiClient.newsApiService.searchNews(query, ApiConfig.API_KEY)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful) {
                        val newsResponse = response.body()
                        newsResponse?.let {
                            onSucess(it.articles)
                        } ?: onFailure("No data available")
                    } else {
                        onFailure("Failed to fetch news")
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    onFailure(t.message ?: "Unknown error")
                }
            })
    }
}