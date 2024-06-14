package com.capstone.padicare.ui.feed

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityFeedAddBinding
import com.capstone.padicare.databinding.ActivityResultBinding

class FeedAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
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