package com.bangkit.freshfuel.model.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("data")
	val predictData: List<String>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Any? = null
)
