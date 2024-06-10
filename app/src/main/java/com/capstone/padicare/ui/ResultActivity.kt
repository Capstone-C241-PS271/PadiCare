package com.capstone.padicare.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityResultBinding
import com.capstone.padicare.ui.history.HistoryFragment

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.result_title)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24_white)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@ResultActivity, R.color.hijau3)))

            val textView = TextView(this@ResultActivity).apply {
                text = getString(R.string.result_title)
                setTextColor(Color.WHITE)
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
            }
            setDisplayShowTitleEnabled(false)
            setCustomView(textView)
            setDisplayShowCustomEnabled(true)
        }


        val imageUriString = intent.getStringExtra("imageUri")
        val predictionResult = intent.getStringExtra("predictionResult")
        val suggestion = intent.getStringExtra("suggestion")

        val glide = Glide.with(this)
        val imageView = findViewById<ImageView>(R.id.iv_result)
        glide.load(imageUriString).into(imageView)

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.ivResult.setImageURI(imageUri)
        }

        binding.tvResult.text = predictionResult
        binding.tvSuggest.text = suggestion
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("navigateTo", "HistoryFragment")
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
