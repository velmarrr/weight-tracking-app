package com.example.weightmonitoringapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.weightmonitoringapp.db.Records
import com.example.weightmonitoringapp.db.RecordsDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.asFlow

class RecordsViewModel(private val recordsDao: RecordsDao) : ViewModel() {

    private val _records = MutableStateFlow<List<Records>>(emptyList())
    val records: StateFlow<List<Records>> = _records.asStateFlow()

    init {
        viewModelScope.launch {
            recordsDao.getAll().asFlow().collect {
                _records.value = it
            }
        }
    }

    fun addRecord(weight: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRecord = Records(0, weight, System.currentTimeMillis())
            recordsDao.insertRecord(newRecord)
        }
    }

    fun deleteRecord(record: Records) {
        viewModelScope.launch(Dispatchers.IO) {
            recordsDao.deleteRecord(record)
        }
    }
}