package com.bangkit.freshfuel.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.UserPreference
import com.bangkit.freshfuel.data.remote.api.ApiService
import com.bangkit.freshfuel.model.request.LoginRequest
import com.bangkit.freshfuel.model.request.RegisterRequest
import com.bangkit.freshfuel.model.response.DefaultResponse
import com.bangkit.freshfuel.model.response.LoginResponse

class UserRepository(private val preference: UserPreference, private val apiService: ApiService) {

    suspend fun loginUser(user: LoginRequest): LiveData<Result<LoginResponse>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.login(user)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.accessToken == "") {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        emit(Result.Success(body))
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

    suspend fun registerUser(request: RegisterRequest): LiveData<Result<DefaultResponse>> =
        liveData {
            try {
                emit(Result.Loading)
                val response = apiService.register(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.message == "") {
                            emit(Result.Error(body.message.toString()))
                        } else {
                            emit(Result.Success(body))
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
        private var instance: UserRepository? = null
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preference, apiService)
            }.also { instance = it }
    }
}