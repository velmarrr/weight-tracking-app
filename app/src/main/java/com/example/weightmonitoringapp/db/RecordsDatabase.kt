package com.example.weightmonitoringapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Records::class], version = 1)
abstract class RecordsDatabase : RoomDatabase() {
    abstract fun getRecordsDao(): RecordsDao

    companion object {
        @Volatile
        private var INSTANCE: RecordsDatabase? = null

        fun getInstance(context: Context): RecordsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecordsDatabase::class.java,
                    "Records_DB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}