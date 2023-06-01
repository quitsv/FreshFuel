package com.bangkit.freshfuel.view.login

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.UserRepository
import com.bangkit.freshfuel.model.request.LoginRequest

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    suspend fun loginUser(loginRequest: LoginRequest) = repository.loginUser(loginRequest)
}