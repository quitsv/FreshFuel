package com.bangkit.freshfuel.model.response

import com.google.gson.annotations.SerializedName

data class ProgressResponse(

	@field:SerializedName("data")
	val data: ProgressData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Any? = null
)

data class ProgressData(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("progress")
	val progress: List<ProgressItem>? = null,

	@field:SerializedName("user")
	val user: String? = null
)

data class ProgressItem(

	@field:SerializedName("recipe")
	val recipe: String? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
