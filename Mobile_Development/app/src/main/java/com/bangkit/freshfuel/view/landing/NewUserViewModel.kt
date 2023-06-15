package com.bangkit.freshfuel.view.landing

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.RecipeRepository
import com.bangkit.freshfuel.model.request.ProgressRequest

class NewUserViewModel(private val repository: RecipeRepository) : ViewModel() {

    suspend fun postProgress(request: ProgressRequest) = repository.postProgress(request)

}