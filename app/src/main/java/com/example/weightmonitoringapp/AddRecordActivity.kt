package com.example.weightmonitoringapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weightmonitoringapp.databinding.ActivityAddRecordBinding
import com.example.weightmonitoringapp.db.Records
import com.example.weightmonitoringapp.db.RecordsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recordsDao = RecordsDatabase.getInstance(this).getRecordsDao()

        binding.saveButton.setOnClickListener {
            val weightText = binding.weightField.text.toString()
            if (weightText.isNotEmpty()) {
                try {
                    val weight = weightText.toFloat()
                    val currentDate = System.currentTimeMillis()

                    lifecycleScope.launch {
                        val newRecord = Records(0, weight, currentDate)
                        withContext(Dispatchers.IO) {
                            recordsDao.insertRecord(newRecord)
                        }
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddRecordActivity, "Record added!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Invalid weight value!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter weight!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.backToMainPageButton.setOnClickListener {
            finish()
        }
    }
}
