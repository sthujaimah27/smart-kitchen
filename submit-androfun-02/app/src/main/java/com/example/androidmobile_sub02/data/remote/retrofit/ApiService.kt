package com.example.androidmobile_sub02.data.remote.retrofit

import com.example.androidmobile_sub02.data.remote.response.DetailEventResource
import com.example.androidmobile_sub02.data.remote.response.GetAllEventResource
import com.example.androidmobile_sub02.data.remote.response.IngredientResponse
import com.example.androidmobile_sub02.data.remote.response.RecipeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/events")
    fun getAllEvent(
        @Query("limit") limit: Int = 10
    ): Call<GetAllEventResource>

    @GET("/events")
    fun getAllFinishEvent(
        @Query("active") active: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<GetAllEventResource>

    @GET("/events")
    fun getAllUpComingEvent(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 10
    ): Call<GetAllEventResource>

    @GET("/events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailEventResource>

    @Multipart
    @POST("/scan-image")
    fun scanImage(
        @Part image: MultipartBody.Part
    ): Call<IngredientResponse>

    // Endpoint for getting recipe recommendations
    @POST("/recommend-recipes")
    fun recommendRecipes(
        @Body body: RequestBody
    ): Call<RecipeResponse>

}
