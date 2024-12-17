package com.example.androidmobile_sub02.ui.finishedEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmobile_sub02.data.remote.response.GetAllEventResource
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.data.remote.response.RecipeResponse
import com.example.androidmobile_sub02.data.remote.retrofit.ApiConfig
import com.example.androidmobile_sub02.ui.upComingEvent.UpComingEventViewModel
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedEventViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _listDataEventFinished = MutableLiveData<List<ListEventsItem>>()
    val listDataEventFinished: LiveData<List<ListEventsItem>> = _listDataEventFinished

    private val _listDataUpcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val listDataUpcomingEvent: LiveData<List<ListEventsItem>> = _listDataUpcomingEvent

    private val _listDataRecipes = MutableLiveData<List<Recipe>>()
    val listDataRecipes: LiveData<List<Recipe>> = _listDataRecipes

    init {
        getAllEventUpComing()
        getRecipes()
    }

    private fun getAllEventUpComing() {
        _isLoadingFinished.value = true
        val client = ApiConfig.getApiService().getAllUpComingEvent()
        client.enqueue(object : Callback<GetAllEventResource> {
            override fun onResponse(
                call: Call<GetAllEventResource>,
                response: Response<GetAllEventResource>
            ) {
                _isLoadingFinished.value = false
                if (response.isSuccessful) {
                    if (response.body() != null){
                        _listDataUpcomingEvent.value = response.body()?.listEvents
                    }
                } else {
                    Log.e(UpComingEventViewModel.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetAllEventResource>, t: Throwable) {
                _isLoadingFinished.value = false
                Log.e(UpComingEventViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getRecipes() {
        _isLoading.value = true
        val reqBodyMap = mapOf("ingredients" to "ayam")

        // Convert Map to JSON string using Gson
        val json = Gson().toJson(reqBodyMap)

        // Create RequestBody from JSON string
        val reqBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().recommendRecipes(reqBody)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.data?.recommendedRecipes?.let { recipes ->
                        _listDataRecipes.value = recipes
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "FINISHED_EVENT_VIEW_MODEL_TAG"
    }
}