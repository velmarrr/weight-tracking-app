package com.example.weightmonitoringapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weightmonitoringapp.databinding.RecordsItemBinding
import com.example.weightmonitoringapp.db.Records
import java.text.SimpleDateFormat
import java.util.*

class RecordsAdapter(
    private var records: MutableList<Records>,
    private val deleteRecord: (Records) -> Unit) :
    RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = RecordsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(record.date))

        holder.binding.weightView.text = "${record.weight} kg"
        holder.binding.dateView.text = formattedDate

        holder.binding.deleteButton.setOnClickListener {
            deleteRecord(record)
        }
    }

    override fun getItemCount() = records.size

    fun updateData(newRecords: List<Records>) {
        records.clear()
        records.addAll(newRecords)
        notifyDataSetChanged()
    }

    class RecordViewHolder(val binding: RecordsItemBinding) : RecyclerView.ViewHolder(binding.root)
}
