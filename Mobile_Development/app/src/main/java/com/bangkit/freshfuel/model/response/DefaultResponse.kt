package com.bangkit.freshfuel.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DefaultResponse(

    @field:SerializedName("message")
    val message: String? = null,
    val error: Boolean? = null

) : Parcelable