package com.bangkit.freshfuel.view.detail

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository

class DetailViewModel(private val repository: RecipeRepository) : ViewModel() {

    suspend fun getRecipeDetail(id: String) = repository.getRecipeDetail(id)
}