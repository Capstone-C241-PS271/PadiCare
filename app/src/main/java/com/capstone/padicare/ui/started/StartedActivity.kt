package com.capstone.padicare.ui.started

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityStartedBinding

class StartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}