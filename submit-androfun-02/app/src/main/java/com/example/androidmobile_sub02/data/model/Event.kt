package com.example.androidmobile_sub02.data.model

import com.google.gson.annotations.SerializedName

data class Event(
    val summary: String? = null,
    val mediaCover: String? = null,
    val registrants: Int? = null,
    val imageLogo: String? = null,
    val link: String? = null,
    val description: String? = null,
    val ownerName: String? = null,
    val cityName: String? = null,
    val quota: Int? = null,
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("beginTime")
    val beginTime: String? = null,

    @field:SerializedName("endTime")
    val endTime: String? = null,

    @field:SerializedName("category")
    val category: String? = null
)
