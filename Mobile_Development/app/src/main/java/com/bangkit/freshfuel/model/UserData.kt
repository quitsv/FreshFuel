package com.bangkit.freshfuel.model

import com.bangkit.freshfuel.model.response.DataUser

data class LoginData(
    val token: String? = "",
    val dataUser: DataUser? = null
)
