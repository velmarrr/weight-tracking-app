package com.example.weightmonitoringapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weightmonitoringapp.databinding.ActivityMainBinding
import com.example.weightmonitoringapp.db.RecordsDatabase
import com.example.weightmonitoringapp.db.Records
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.lifecycleScope

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recordsAdapter: RecordsAdapter
    private val database by lazy { RecordsDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recordsAdapter = RecordsAdapter(mutableListOf()) { record ->
            deleteRecord(record)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = recordsAdapter

        observeRecords()

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddRecordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeRecords() {
        database.getRecordsDao().getAll().observe(this) { records ->
            records?.let {
                recordsAdapter.updateData(it)
            }
        }
    }

    private fun deleteRecord(record: Records) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                database.getRecordsDao().deleteRecord(record)
            }
        }
    }
}
