package com.capstone.padicare.data.repo

import com.capstone.padicare.data.api.ApiClient
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.newsresponse.Article
import com.capstone.padicare.newsresponse.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {

    fun seacrhNews(
        onSucess: (List<Article>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val query = "agriculture"
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