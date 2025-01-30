package com.example.weightmonitoringapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordsDao {
    @Query("SELECT * FROM records_table ORDER BY date DESC")
    fun getAll(): LiveData<List<Records>>

    @Insert
    fun insertRecord(record: Records)

    @Delete
    fun deleteRecord(record: Records)

}