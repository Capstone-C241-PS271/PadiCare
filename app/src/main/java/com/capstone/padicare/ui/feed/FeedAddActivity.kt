package com.capstone.padicare.ui.feed

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.capstone.padicare.R
import com.capstone.padicare.data.response.PostRequest
import com.capstone.padicare.databinding.ActivityFeedAddBinding
import com.capstone.padicare.model.ViewModelFactory

class FeedAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedAddBinding
    private val postViewModel: FeedViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.btnUpload.setOnClickListener {
            val title = binding.titleText.text.toString().trim()
            val content = binding.descText.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                sharedPreferences = this.getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
                var token = sharedPreferences.getString("token", "") ?: ""
                token = "Bearer $token"
                val post = PostRequest(
                    title = title,
                    content = content
                )
                postViewModel.createPost(token, post)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        postViewModel.feedResult.observe(this, Observer { result ->
            result.onSuccess {
                Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure {
                Toast.makeText(this, "Failed to create post", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = getString(R.string.post)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24_white)
            setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this@FeedAddActivity,
                        R.color.hijau3
                    )
                )
            )

            val textView = TextView(this@FeedAddActivity).apply {
                text = getString(R.string.post)
                setTextColor(Color.WHITE)
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
            }
            setDisplayShowTitleEnabled(false)
            setCustomView(textView)
            setDisplayShowCustomEnabled(true)
        }
    }
}
