package com.fedorov.alex.app4test.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fedorov.alex.app4test.R
import com.fedorov.alex.app4test.data.model.Record
import kotlinx.android.synthetic.main.item_record.view.*

class MainAdapter(val onItemClick: ((Record) -> Unit)? = null) :
    RecyclerView.Adapter<MainAdapter.RecordViewHolder>() {

    var records: List<Record> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_record, parent, false)

        return RecordViewHolder(view)
    }

    override fun getItemCount() = records.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(record: Record) = with(record) {
            itemView.title.text = this.filename
            itemView.btnPlayRecord.setOnClickListener {
                onItemClick?.invoke(record)
            }
        }
    }
}