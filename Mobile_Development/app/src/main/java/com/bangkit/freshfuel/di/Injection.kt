package com.bangkit.freshfuel.di

import android.content.Context
import com.bangkit.freshfuel.data.UserPreference
import com.bangkit.freshfuel.data.remote.api.ApiConfig
import com.bangkit.freshfuel.data.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preference = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(preference, apiService)
    }
}