package com.example.androidmobile_sub02.ui.detailEvent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmobile_sub02.data.local.entity.EventEntity
import com.example.androidmobile_sub02.data.remote.response.DetailEventResource
import com.example.androidmobile_sub02.data.remote.retrofit.ApiConfig
import com.example.androidmobile_sub02.data.repo.EventRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel(
    private val eventRepository: EventRepository
): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailEvent = MutableLiveData<DetailEventResource>()
    val detailEvent: LiveData<DetailEventResource> = _detailEvent

    fun insertEventFav(event: EventEntity){
        viewModelScope.launch {
            eventRepository.insertFavEvent(event)
        }
    }

    fun deleteEventFav(eventId: Int){
        viewModelScope.launch {
            eventRepository.deleteFavEvent(eventId)
        }
    }

    fun isEventFav(eventId: Int): LiveData<Boolean> = eventRepository.isFavEvent(eventId)

    fun getDetailEvent(id: Int){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(id)
        client.enqueue(object : Callback<DetailEventResource>{
            override fun onResponse(
                call: Call<DetailEventResource>,
                response: Response<DetailEventResource>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody != null){
                        _detailEvent.value = response.body()
                    }
                }else{
                    Log.e(TAG, "onResponseFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResource>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        const val TAG = "DetailEventViewModel"
    }
}