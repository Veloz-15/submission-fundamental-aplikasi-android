package com.dicoding.submission1.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.submission1.database.FavEventDao
import com.dicoding.submission1.database.FavEventDatabase
import com.dicoding.submission1.database.FavoriteEvent

class FavEventRepository(application: Application) {
    private val favEventDao: FavEventDao

    init {
        val db = FavEventDatabase.getDatabase(application)
        favEventDao = db.favEventDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEvent>> = favEventDao.getAllFavorites()

    suspend fun insert(event: FavoriteEvent) {
        favEventDao.insert(event)
    }

    suspend fun delete(event: FavoriteEvent) {
        favEventDao.delete(event)
    }
}
