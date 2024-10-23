package com.dicoding.submission1.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_event")
@Parcelize
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var mediaCover: String? = null,
    var summary: String = "",
    var registrants: Int = 0,
    var imageLogo: String? = null,
    var link: String = "",
    var description: String = "",
    var ownerName: String = "",
    var cityName: String = "",
    var quota: Int = 0,
    var beginTime: String = "",
    var endTime: String = "",
    var category: String = ""
) : Parcelable