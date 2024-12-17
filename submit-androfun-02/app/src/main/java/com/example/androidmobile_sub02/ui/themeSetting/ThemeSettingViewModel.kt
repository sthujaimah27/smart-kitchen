package com.example.androidmobile_sub02.ui.themeSetting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidmobile_sub02.data.DatastorePref
import com.example.androidmobile_sub02.data.repo.EventRepository
import kotlinx.coroutines.launch

class ThemeSettingViewModel(
    private val dataStore: DatastorePref
): ViewModel(){
    fun getThemeSetting() = dataStore.getThemeSetting()

    fun saveThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            dataStore.saveThemeSetting(isDarkModeActive)
        }
    }
}