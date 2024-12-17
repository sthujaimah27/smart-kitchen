package com.example.androidmobile_sub02.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidmobile_sub02.data.local.entity.EventEntity

@Database(entities = [EventEntity::class], version = 1, exportSchema = true)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object{
        const val DATABASE_NAME = "event_db"

        @Volatile
        private var INSTANCE: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, DATABASE_NAME
                ).build()
            }
    }
}