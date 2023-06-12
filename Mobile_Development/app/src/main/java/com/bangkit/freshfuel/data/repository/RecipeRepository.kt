package com.bangkit.freshfuel.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.UserPreference
import com.bangkit.freshfuel.data.remote.api.ApiService

class RecipeRepository(private val preference: UserPreference, private val apiService: ApiService) {

    suspend fun search(q: String): LiveData<Result<List<String>>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.search(q)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.data != null) {
                            emit(Result.Success((body.data)))
                        } else {
                            emit(Result.Error("Data is null"))
                        }
                    }
                } else {
                    emit(Result.Error("Body is null"))
                }
            } else {
                emit(Result.Error("Response is not successful"))
            }
        } catch (exception: Exception) {
            emit(Result.Error(exception.message.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: RecipeRepository? = null
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService
        ): RecipeRepository =
            instance ?: synchronized(this) {
                instance ?: RecipeRepository(preference, apiService)
            }.also { instance = it }
    }
}