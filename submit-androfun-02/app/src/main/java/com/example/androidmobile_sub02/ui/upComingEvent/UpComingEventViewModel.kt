package com.example.androidmobile_sub02.ui.upComingEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmobile_sub02.data.remote.response.GetAllEventResource
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpComingEventViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUpComingEvent = MutableLiveData<List<ListEventsItem>>()
    val listUpComingEvent: LiveData<List<ListEventsItem>> = _listUpComingEvent

    init {
        getAllEventUpComing()
    }

    private fun getAllEventUpComing() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllUpComingEvent()
        client.enqueue(object : Callback<GetAllEventResource> {
            override fun onResponse(
                call: Call<GetAllEventResource>,
                response: Response<GetAllEventResource>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body() != null){
                        _listUpComingEvent.value = response.body()?.listEvents
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetAllEventResource>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object{
        const val TAG = "UPCOMING_EVENT_VIEW_MODEL_TAG"
    }

}