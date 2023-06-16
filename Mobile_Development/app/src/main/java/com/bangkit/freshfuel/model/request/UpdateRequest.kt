package com.bangkit.freshfuel.model.request

data class UpdateRequest(
    val date: String? = null,
    val lunchMenu: String? = null,
    val dinnerMenu: String? = null,
    val rating: Int? = null
)