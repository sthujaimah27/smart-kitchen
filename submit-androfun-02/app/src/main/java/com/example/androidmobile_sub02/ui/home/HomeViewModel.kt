package com.example.androidmobile_sub02.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmobile_sub02.data.remote.response.GetAllEventResource
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.data.remote.retrofit.ApiConfig
import com.example.androidmobile_sub02.ui.finishedEvent.FinishedEventViewModel
import com.example.androidmobile_sub02.ui.finishedEvent.FinishedEventViewModel.Companion
import com.example.androidmobile_sub02.ui.upComingEvent.UpComingEventViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _isLoadingUpComing = MutableLiveData<Boolean>()
    val isLoadingUpComing: LiveData<Boolean> = _isLoadingUpComing

    private val _isLoadingFinished = MutableLiveData<Boolean>()
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _listDataUpcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val listDataUpcomingEvent: LiveData<List<ListEventsItem>> = _listDataUpcomingEvent

    private val _listDataFinishedEvent = MutableLiveData<List<ListEventsItem>>()
    val listDataFinishedEvent: LiveData<List<ListEventsItem>> = _listDataFinishedEvent

    init {
        getAllEventFinished()
        getAllEventUpComing()
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

    private fun getAllEventFinished() {
        _isLoadingUpComing.value = true
        val client = ApiConfig.getApiService().getAllFinishEvent()
        client.enqueue(object : Callback<GetAllEventResource> {
            override fun onResponse(
                call: Call<GetAllEventResource>,
                response: Response<GetAllEventResource>
            ){
                _isLoadingUpComing.value = false
                if(response.isSuccessful){
                    if(response.body() != null){
                        _listDataFinishedEvent.value = response.body()?.listEvents
                    }
                }else{
                    Log.e(FinishedEventViewModel.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GetAllEventResource>, t: Throwable) {
                _isLoadingUpComing.value = false
                Log.e(FinishedEventViewModel.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object{
        const val TAG = "HOME_VIEW_MODEL_TAG"
    }
}