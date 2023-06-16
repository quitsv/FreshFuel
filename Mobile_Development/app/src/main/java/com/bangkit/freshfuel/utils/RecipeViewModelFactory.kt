package com.bangkit.freshfuel.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.freshfuel.data.repository.RecipeRepository
import com.bangkit.freshfuel.di.Injection
import com.bangkit.freshfuel.view.detail.DetailViewModel
import com.bangkit.freshfuel.view.landing.NewUserViewModel
import com.bangkit.freshfuel.view.main.ui.home.HomeViewModel
import com.bangkit.freshfuel.view.main.ui.recipe.RecipeViewModel
import com.bangkit.freshfuel.view.rating.RatingViewModel

class RecipeViewModelFactory private constructor(private val repository: RecipeRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RatingViewModel::class.java) -> {
                RatingViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RecipeViewModel::class.java) -> {
                RecipeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(NewUserViewModel::class.java) -> {
                NewUserViewModel(repository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: RecipeViewModelFactory? = null
        fun getInstance(context: Context): RecipeViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: RecipeViewModelFactory(Injection.provideRecipeRepository(context))
            }.also { instance = it }
    }
}