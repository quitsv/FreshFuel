package com.bangkit.freshfuel.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("dataUser")
	val dataUser: DataUser? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("accessToken")
	val accessToken: String? = null
)

data class DataUser(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
