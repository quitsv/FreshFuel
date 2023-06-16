package com.bangkit.freshfuel.data.preference

import android.content.Context
import com.google.gson.Gson

class RecipePreference(context: Context) {
    private val preference = context.getSharedPreferences("recipe", Context.MODE_PRIVATE)

    fun setRecipe(data: List<String>) {
        val gson = Gson()
        val json = gson.toJson(data)

        preference.edit().apply {
            putString("recommend", json)
        }.apply()
    }

    fun getRecipes(): List<String> {
        val gson = Gson()
        val json = preference.getString("recommend", null)

        val recipes = gson.fromJson(json, Array<String>::class.java).toList()
        return recipes.map { it.trim() }
    }

    fun setRecentRecipe(recipe: String) {
        preference.edit().apply {
            putString("recent", recipe)
        }.apply()
    }

    fun getRecentRecipe(): String? = preference.getString("recent", null)

    companion object {
        @Volatile
        private var INSTANCE: RecipePreference? = null

        fun getInstance(context: Context): RecipePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = RecipePreference(context)
                INSTANCE = instance
                instance
            }
        }
    }
}