package com.bangkit.freshfuel.view.main.ui.home

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository

class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    val userData = repository.getUser()

    suspend fun getCurrentProgress(email: String, date: String) = repository.getCurrentProgress(email, date)
}