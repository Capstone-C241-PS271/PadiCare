package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class CommentResponse(
	val id: Int,
	val author: String,
	val content: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,
)
