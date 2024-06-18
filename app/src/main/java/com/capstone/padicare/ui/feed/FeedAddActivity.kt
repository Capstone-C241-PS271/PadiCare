package com.capstone.padicare.ui.feed

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.capstone.padicare.MainActivity
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
                binding.progressBar.visibility = View.VISIBLE

                postViewModel.createPost(token, post)
            } else {
                showAlert(getString(R.string.fill_all_fields_message))
            }
        }

        postViewModel.feedResult.observe(this, Observer { result ->
            binding.progressBar.visibility = View.GONE

            result.onSuccess {
                Toast.makeText(this, R.string.post_created_successfully, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }.onFailure {
                Toast.makeText(this, R.string.post_creation_failed, Toast.LENGTH_SHORT).show()
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

    private fun showAlert(message: String) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}
