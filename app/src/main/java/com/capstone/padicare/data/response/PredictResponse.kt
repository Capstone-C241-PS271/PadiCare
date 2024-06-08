package com.capstone.padicare.data.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PredictRequest(
	@SerializedName("image") val image: String
)

data class Data(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("suggestion")
	val suggestion: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("author_id")
	val authorId: Int? = null
)
