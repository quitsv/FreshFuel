package com.bangkit.freshfuel.view.main.ui.recipe

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    suspend fun search(q: String) = repository.search(q)
}