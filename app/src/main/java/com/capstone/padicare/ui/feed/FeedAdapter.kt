package com.capstone.padicare.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.padicare.R
import com.capstone.padicare.data.response.Data
import com.capstone.padicare.data.response.PostResponse
import com.capstone.padicare.helper.toDateFormat

class FeedAdapter(private val feedList: List<PostResponse>?) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>(){

    class FeedViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameFeedTextView: TextView = itemView.findViewById(R.id.nameFeed)
        val titleFeedTextView: TextView = itemView.findViewById(R.id.titleFeed)
        val createAtTextView: TextView = itemView.findViewById(R.id.createat)
        val storyAddTextView: TextView = itemView.findViewById(R.id.storyAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return FeedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feedList?.size ?: 0
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = feedList?.get(position)
        holder.nameFeedTextView.text = feed?.author.toString()
        holder.titleFeedTextView.text = feed?.title
        holder.createAtTextView.text = feed?.createdAt?.toDateFormat()
        holder.storyAddTextView.text = feed?.content
    }
}