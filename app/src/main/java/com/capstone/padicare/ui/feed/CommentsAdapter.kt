package com.capstone.padicare.ui.feed


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.padicare.data.response.CommentResponse
import com.capstone.padicare.databinding.CommentItemBinding

class CommentsAdapter(
    private val comments: ArrayList<CommentResponse>
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private lateinit var binding: CommentItemBinding

    inner class CommentViewHolder(val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        binding = CommentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CommentViewHolder(binding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = comments[position]
        binding.name.text = item.author
        binding.content.text = item.content
    }

    @SuppressLint("NotifyDataSetChanged")
    fun dispatch(data: ArrayList<CommentResponse>) {
        comments.clear()
        comments.addAll(data)
        notifyDataSetChanged()
    }
}