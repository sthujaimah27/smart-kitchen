package com.example.androidmobile_sub02.ui.favEvent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidmobile_sub02.data.local.entity.EventEntity
import com.example.androidmobile_sub02.data.repo.EventRepository

class FavoriteEventViewModel(
    private val repository: EventRepository
): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
    }

    fun getAllFavEvent(): LiveData<List<EventEntity>> {
        _isLoading.value = false
        return repository.getAllFavEvent()
    }

}