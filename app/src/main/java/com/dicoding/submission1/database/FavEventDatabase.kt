package com.dicoding.submission1.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [FavoriteEvent::class], version = 2)
abstract class FavEventDatabase : RoomDatabase() {
    abstract fun favEventDao(): FavEventDao

    companion object {
        @Volatile
        private var INSTANCE: FavEventDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavEventDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavEventDatabase::class.java,
                    "favorite_event_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
