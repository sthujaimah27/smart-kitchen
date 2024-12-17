package com.example.androidmobile_sub02.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidmobile_sub02.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM event ORDER BY id ASC")
    fun getAllEventFav(): LiveData<List<EventEntity>>


    @Query("SELECT EXISTS(SELECT * FROM event WHERE id = :id)")
    fun isEventFavById(id: Int): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventFav(event: EventEntity)

    @Query("DELETE FROM event WHERE id = :id")
    suspend fun deleteEventFavById(id: Int)
}