package com.capstone.padicare.database

import com.capstone.padicare.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDao {
    @GET("/v2/everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>
}