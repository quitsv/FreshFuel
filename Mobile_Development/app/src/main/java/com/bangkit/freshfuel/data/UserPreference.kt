package com.bangkit.freshfuel.data

import android.content.Context
import com.bangkit.freshfuel.model.LoginData
import com.bangkit.freshfuel.model.response.DataUser
import com.bangkit.freshfuel.model.response.LoginResponse

class UserPreference(context: Context) {
    private val preference = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    fun setUser(data: LoginResponse) {
        preference.edit().apply {
            putString("token", data.accessToken)
            putString("name", data.dataUser!!.name)
            putString("email", data.dataUser.email)
            putString("allergies", data.dataUser.allergies)
        }.apply()
    }

    fun getUser(): LoginData {
        val token = preference.getString("token", "")
        val name = preference.getString("name", "")
        val email = preference.getString("email", "")
        val allergies = preference.getString("allergies", "")
        return LoginData(
            token = token,
            dataUser = DataUser(
                allergies = allergies,
                name = name,
                email = email
            )
        )
    }

    fun logout() {
        preference.edit().apply {
            putString("token", "")
            putString("name", "")
            putString("email", "")
            putString("allergies", "")
        }.apply()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(context: Context): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(context)
                INSTANCE = instance
                instance
            }
        }
    }
}