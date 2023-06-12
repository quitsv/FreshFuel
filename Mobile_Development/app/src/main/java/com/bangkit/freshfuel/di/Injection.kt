package com.bangkit.freshfuel.di

import android.content.Context
import com.bangkit.freshfuel.data.UserPreference
import com.bangkit.freshfuel.data.remote.api.ApiConfig
import com.bangkit.freshfuel.data.repository.RecipeRepository
import com.bangkit.freshfuel.data.repository.UserRepository

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val preference = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(preference, apiService)
    }
    fun provideRecipeRepository(context: Context): RecipeRepository {
        val preference = UserPreference(context)
        val apiService = ApiConfig.getApiService()
        return RecipeRepository.getInstance(preference, apiService)
    }
}