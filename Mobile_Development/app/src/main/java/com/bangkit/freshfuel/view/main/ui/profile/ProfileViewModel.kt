package com.bangkit.freshfuel.view.main.ui.profile

import androidx.lifecycle.ViewModel
import com.bangkit.freshfuel.data.repository.UserRepository

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    suspend fun getUser() = repository.getUser()
    fun logout() = repository.logout()
}