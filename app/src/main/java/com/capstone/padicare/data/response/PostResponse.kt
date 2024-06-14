package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class CreatedResponse(
    @field:SerializedName("message")
    val message: String
)

data class PostResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("authorId")
    val authorId: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)

