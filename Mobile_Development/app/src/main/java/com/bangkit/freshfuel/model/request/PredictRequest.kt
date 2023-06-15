package com.bangkit.freshfuel.model.request

data class PredictRequest(
    val recipeName: String,
    val allergies: String,
    val rating: Int
)
