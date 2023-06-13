package com.bangkit.freshfuel.model.response

import com.google.gson.annotations.SerializedName

data class RecipeDetailResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	val error: Any? = null
)

data class Data(

	@field:SerializedName("technique")
	val technique: List<String>? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String>? = null,

	@field:SerializedName("information")
	val information: Information? = null,

	@field:SerializedName("properties")
	val properties: List<String>? = null
)

data class Information(

	@field:SerializedName("sodium")
	val sodium: Int? = null,

	@field:SerializedName("protein")
	val protein: Int? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("fat")
	val fat: Int? = null,

	@field:SerializedName("calories")
	val calories: Int? = null,

	@field:SerializedName("Recipe_Name")
	val recipeName: String? = null
)
