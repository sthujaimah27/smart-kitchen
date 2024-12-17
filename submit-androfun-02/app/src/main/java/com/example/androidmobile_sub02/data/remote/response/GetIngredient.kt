package com.example.androidmobile_sub02.data.remote.response

import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: IngredientData? = null
)

data class IngredientData(
    @field:SerializedName("ingredient")
    val ingredient: String? = null
)