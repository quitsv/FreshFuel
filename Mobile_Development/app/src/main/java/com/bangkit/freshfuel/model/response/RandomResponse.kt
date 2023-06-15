package com.bangkit.freshfuel.model.response

import com.google.gson.annotations.SerializedName

data class RandomResponse(

	@field:SerializedName("data")
	val randomData: List<String>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Any? = null
)
