package com.bangkit.freshfuel.data.remote.api.model

import com.bangkit.freshfuel.model.request.PredictRequest
import com.bangkit.freshfuel.model.response.PredictResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ModelApiService {
    @POST("predict")
    suspend fun postRating(
        @Body predictRequest: PredictRequest
    ): Response<PredictResponse>
}
