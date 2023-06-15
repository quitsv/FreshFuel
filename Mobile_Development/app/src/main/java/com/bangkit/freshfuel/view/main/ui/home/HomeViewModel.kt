package com.bangkit.freshfuel.view.main.ui.home

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository
import com.bangkit.freshfuel.model.request.ProgressRequest

class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    val userData = repository.getUser()

    suspend fun getHistory(email: String) = repository.getHistory(email)

    suspend fun getCurrentProgress(email: String, date: String) = repository.getCurrentProgress(email, date)

    suspend fun getRandom(allergies: String) = repository.getRandom(allergies)

    suspend fun postProgress(request: ProgressRequest) = repository.postProgress(request)
}