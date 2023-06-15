package com.bangkit.freshfuel.data.remote.api

import com.bangkit.freshfuel.model.request.LoginRequest
import com.bangkit.freshfuel.model.request.ProgressRequest
import com.bangkit.freshfuel.model.request.RegisterRequest
import com.bangkit.freshfuel.model.request.UpdateRequest
import com.bangkit.freshfuel.model.response.DefaultResponse
import com.bangkit.freshfuel.model.response.HistoryResponse
import com.bangkit.freshfuel.model.response.LoginResponse
import com.bangkit.freshfuel.model.response.PredictResponse
import com.bangkit.freshfuel.model.response.ProgressResponse
import com.bangkit.freshfuel.model.response.RecipeDetailResponse
import com.bangkit.freshfuel.model.response.SearchRecipesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body registrationRequest: RegisterRequest
    ): Response<DefaultResponse>

    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("search")
    suspend fun search(
        @Query("recipeName") query: String
    ): Response<SearchRecipesResponse>

    @GET("getRecipe")
    suspend fun getRecipeDetail(
        @Query("recipeName") name: String
    ): Response<RecipeDetailResponse>

    @GET("progress/user/{email}")
    suspend fun getCurrentProgress(
        @Path("email") email: String,
        @Query("date") date: String
    ): Response<ProgressResponse>

    @GET("progress/user/{email}")
    suspend fun getHistory(
        @Path("email") email: String
    ): Response<HistoryResponse>

    @GET("generateRandom")
    suspend fun getRandom(
        @Query("allergies") allergies: String? = null
    ): Response<PredictResponse>

    @POST("progress/user/{email}")
    suspend fun postProgress(
        @Path("email") email: String,
        @Body newProgress: ProgressRequest
    ): Response<HistoryResponse>

    @PUT("progress/user/{email}")
    suspend fun updateProgress(
        @Path("email") email: String,
        @Body request: UpdateRequest
    ): Response<ProgressResponse>
}