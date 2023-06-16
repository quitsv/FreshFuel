package com.bangkit.freshfuel.view.register

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.UserRepository
import com.bangkit.freshfuel.model.request.RegisterRequest

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    suspend fun register(request: RegisterRequest) = repository.registerUser(request)
}