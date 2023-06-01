package com.bangkit.freshfuel.data.remote.api

import com.bangkit.freshfuel.model.request.LoginRequest
import com.bangkit.freshfuel.model.request.RegisterRequest
import com.bangkit.freshfuel.model.response.DefaultResponse
import com.bangkit.freshfuel.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body registrationRequest: RegisterRequest
    ): Response<DefaultResponse>

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}