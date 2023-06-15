package com.bangkit.freshfuel.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.freshfuel.data.Result
import com.bangkit.freshfuel.data.preference.UserPreference
import com.bangkit.freshfuel.data.remote.api.ApiService
import com.bangkit.freshfuel.data.remote.api.model.ModelApiConfig
import com.bangkit.freshfuel.data.remote.api.model.ModelApiService
import com.bangkit.freshfuel.model.request.PredictRequest
import com.bangkit.freshfuel.model.request.ProgressRequest
import com.bangkit.freshfuel.model.request.UpdateRequest
import com.bangkit.freshfuel.model.response.ProgressData
import com.bangkit.freshfuel.model.response.ProgressItem
import com.bangkit.freshfuel.model.response.RecipeData

class RecipeRepository(private val preference: UserPreference, private val apiService: ApiService) {

    suspend fun updateProgress(request: UpdateRequest): LiveData<Result<List<ProgressItem>>> =
        liveData {
            try {
                emit(Result.Loading)
                val email = preference.getUser().dataUser?.email!!
                val response = apiService.updateProgress(email, request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error != null) {
                            emit(Result.Error(body.message.toString()))
                        } else {
                            if (body.data != null) {
                                emit(Result.Success((body.data.progress!!)))
                            } else {
                                emit(Result.Error("Body is null"))
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

    suspend fun postRating(request: PredictRequest): LiveData<Result<List<String>>> = liveData {
        val modelApiService: ModelApiService = ModelApiConfig.getApiService()
        try {
            emit(Result.Loading)
            val response = modelApiService.postRating(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.predictData != null) {
                            emit(Result.Success(body.predictData))
                        } else {
                            emit(Result.Error("Body is null"))
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

    suspend fun postProgress(request: ProgressRequest): LiveData<Result<ProgressData>> =
        liveData {
            try {
                emit(Result.Loading)
                val email = preference.getUser().dataUser?.email!!
                val response = apiService.postProgress(email, request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.error != null) {
                            emit(Result.Error(body.message.toString()))
                        } else {
                            if (body.data != null) {
                                emit(Result.Success(body.data))
                            } else {
                                emit(Result.Error("Body is null"))
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
                Log.e("Error", exception.message.toString())
            }
        }

    suspend fun getRandom(allergies: String): LiveData<Result<List<String>>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.getRandom(allergies)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.predictData != null) {
                            emit(Result.Success((body.predictData)))
                        } else {
                            emit(Result.Error("Body is null"))
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

    suspend fun getHistory(email: String): LiveData<Result<List<ProgressData>>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.getHistory(email)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.data != null) {
                            emit(Result.Success((body.data)))
                        } else {
                            emit(Result.Success(emptyList()))
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

    suspend fun getCurrentProgress(
        email: String,
        date: String
    ): LiveData<Result<List<ProgressItem>>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.getCurrentProgress(email, date)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.data != null) {
                            emit(Result.Success((body.data.progress!!)))
                        } else {
                            emit(Result.Success(emptyList()))
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

    fun getUser() = preference.getUser()

    suspend fun getRecipeDetail(name: String): LiveData<Result<RecipeData>> = liveData {
        try {
            emit(Result.Loading)
            val response = apiService.getRecipeDetail(name)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.error != null) {
                        emit(Result.Error(body.message.toString()))
                    } else {
                        if (body.recipeData != null) {
                            emit(Result.Success((body.recipeData)))
                        } else {
                            emit(Result.Error("RecipeData is null"))
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
                            emit(Result.Error("Recipe data is null"))
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