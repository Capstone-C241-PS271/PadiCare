package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class CreatedResponse(
    @field:SerializedName("message")
    val message: String
)

data class PostResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("author_id")
    val authorId: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String
)

