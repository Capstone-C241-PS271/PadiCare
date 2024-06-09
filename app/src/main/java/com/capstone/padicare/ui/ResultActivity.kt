package com.capstone.padicare.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.result_title)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_black_24)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
}
