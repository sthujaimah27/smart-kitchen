package com.example.androidmobile_sub02.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event")
class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "summary")
    val summary: String? = null,

    @ColumnInfo(name = "beginTime")
    val beginTime: String? = null,

    @ColumnInfo(name = "endTime")
    val endTime: String? = null,

    @ColumnInfo(name = "ownerName")
    val ownerName: String? = null,

    @ColumnInfo(name = "cityName")
    val cityName: String? = null,

    @ColumnInfo(name = "quota")
    val quota: Int? = null,

    @ColumnInfo(name = "category")
    val category: String? = null,

    @ColumnInfo(name = "imageLogo")
    val imageLogo: String? = null,

    @ColumnInfo(name = "mediaCover")
    val mediaCover: String? = null,

    @ColumnInfo(name = "link")
    val link: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "registrants")
    val registrants: Int? = null,

)