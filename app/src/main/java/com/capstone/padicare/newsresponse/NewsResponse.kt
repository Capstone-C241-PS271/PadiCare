package com.capstone.padicare.newsresponse

data class NewsResponse(
    val articles: List<Article>
)

data class Article(
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String
)