package com.example.androidmobile_sub02.di

import android.content.Context
import com.example.androidmobile_sub02.data.local.database.EventDatabase
import com.example.androidmobile_sub02.data.repo.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(dao)
    }
}