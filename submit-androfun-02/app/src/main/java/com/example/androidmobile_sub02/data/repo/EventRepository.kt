package com.example.androidmobile_sub02.data.repo

import com.example.androidmobile_sub02.data.local.database.EventDao
import com.example.androidmobile_sub02.data.local.entity.EventEntity

class EventRepository private constructor(
    private val eventDao: EventDao
){

    suspend fun deleteFavEvent(eventId: Int){
        eventDao.deleteEventFavById(eventId)
    }

    suspend fun insertFavEvent(event: EventEntity){
        eventDao.insertEventFav(event)
    }

    fun isFavEvent(eventId: Int) = eventDao.isEventFavById(eventId)

    fun getAllFavEvent() = eventDao.getAllEventFav()

   companion object{
       private var instance: EventRepository? = null
       fun getInstance(
           eventDao: EventDao
       ): EventRepository = instance ?: synchronized(this){
           instance ?: EventRepository(eventDao)
       }.also { instance = it }
   }
}