package com.example.weightmonitoringapp.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "records_table")
data class Records(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "weight") var weight: Float,
    @ColumnInfo(name = "date") var date: Long
)