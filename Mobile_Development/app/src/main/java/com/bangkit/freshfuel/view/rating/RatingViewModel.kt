package com.bangkit.freshfuel.view.rating

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository
import com.bangkit.freshfuel.model.request.PredictRequest
import com.bangkit.freshfuel.model.request.UpdateRequest

class RatingViewModel(private val repository: RecipeRepository) : ViewModel() {

    suspend fun getRecipeDetail(recipeName: String) = repository.getRecipeDetail(recipeName)

    suspend fun postRating(request: PredictRequest) = repository.postRating(request)

    val userData = repository.getUser()

    suspend fun getCurrentProgress(email: String, date: String) =
        repository.getCurrentProgress(email, date)

    suspend fun updateProgress(request: UpdateRequest) = repository.updateProgress(request)
}