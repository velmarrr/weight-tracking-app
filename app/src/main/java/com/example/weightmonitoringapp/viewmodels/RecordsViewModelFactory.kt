package com.example.weightmonitoringapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weightmonitoringapp.db.RecordsDao

class RecordsViewModelFactory(private val recordsDao: RecordsDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecordsViewModel(recordsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}