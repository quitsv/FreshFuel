package com.bangkit.freshfuel.model.request

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val allergies: String
)