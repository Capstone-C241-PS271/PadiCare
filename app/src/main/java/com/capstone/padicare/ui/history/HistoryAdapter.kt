package com.capstone.padicare.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.padicare.R
import com.capstone.padicare.data.response.Data

class HistoryAdapter(private val historyList: List<Data>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_history)
        val resultTextView: TextView = itemView.findViewById(R.id.tv_label)
        val createdAtTextView: TextView = itemView.findViewById(R.id.createdAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = historyList[position]

        // Load image using a library like Glide or Picasso
        Glide.with(holder.imageView.context)
            .load(history.image)
            .into(holder.imageView)

        holder.resultTextView.text = "Hasil: ${history.result}"
        holder.createdAtTextView.text = history.createdAt
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}