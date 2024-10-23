package com.dicoding.submission1.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: FavoriteEvent)

    @Delete
    suspend fun delete(event: FavoriteEvent)

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteEvent?

    @Query("SELECT * FROM favorite_event")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>
}
