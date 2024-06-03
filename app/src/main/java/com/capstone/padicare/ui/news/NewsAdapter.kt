package com.capstone.padicare.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.response.NewsItem

class NewsAdapter : ListAdapter<NewsItem, NewsAdapter.ViewHolder>(DiffCallback()){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitleNews: TextView = itemView.findViewById(R.id.tv_TitleNews)
        private val imgNews: ImageView = itemView.findViewById(R.id.img_News)

        fun bind(newsItem: NewsItem) {
            tvTitleNews.text = newsItem.title
            itemView.setOnClickListener {
                newsItem.url?.let { url ->
                    (itemView.context as MainActivity).openUrl(url)
                }
            }
            if (newsItem.imageUrl != null && newsItem.imageUrl.isNotBlank()) {
                Glide.with(itemView.context)
                    .load(newsItem.imageUrl)
                    .placeholder(R.drawable.ic_baseline_insert_photo_24)
                    .error(R.drawable.ic_baseline_insert_photo_24)
                    .into(imgNews)
            } else {
                imgNews.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_baseline_insert_photo_24
                    )
                )
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val newItem = getItem(position)
        holder.bind(newItem)
    }

    class DiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}