package com.example.androidmobile_sub02.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidmobile_sub02.data.DatastorePref
import com.example.androidmobile_sub02.data.dataStore
import com.example.androidmobile_sub02.data.repo.EventRepository
import com.example.androidmobile_sub02.di.Injection
import com.example.androidmobile_sub02.ui.detailEvent.DetailEventViewModel
import com.example.androidmobile_sub02.ui.favEvent.FavoriteEventViewModel
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val preferences: DatastorePref
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteEventViewModel::class.java)){
            return FavoriteEventViewModel(eventRepository) as T
        } else if(modelClass.isAssignableFrom(ThemeSettingViewModel::class.java)){
            return ThemeSettingViewModel(preferences) as T
        } else if(modelClass.isAssignableFrom(DetailEventViewModel::class.java)){
            return DetailEventViewModel(eventRepository) as T
        }
        return super.create(modelClass)
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: ViewModelFactory(Injection.provideRepository(context), DatastorePref.getInstance(context.dataStore))
            }.also { INSTANCE = it }
    }
}