package com.example.androidmobile_sub02.data.remote.response

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("data")
    val data: RecipeData? = null
)

data class RecipeData(
    @field:SerializedName("detected_ingredient")
    val detectedIngredient: String? = null,

    @field:SerializedName("recommended_recipes")
    val recommendedRecipes: List<Recipe>? = null
)

data class Recipe(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("loves")
    val loves: Int? = null,

    @field:SerializedName("ingredients")
    val ingredients: List<String>? = null,

    @field:SerializedName("instructions")
    val instructions: List<String>? = null,

    @field:SerializedName("source")
    val source: String? = null
)