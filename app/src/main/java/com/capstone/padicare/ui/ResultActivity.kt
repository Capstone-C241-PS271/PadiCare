package com.capstone.padicare.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        Log.d("ResultActivity", "Received Image URI: $imageUriString")
        Log.d("ResultActivity", "Received Prediction Result: $predictionResult")
        Log.d("ResultActivity", "Received Suggestion: $suggestion")

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.ivResult.setImageURI(imageUri)
        }

        binding.tvResult.text = predictionResult
        binding.tvSuggest.text = suggestion
    }
}
