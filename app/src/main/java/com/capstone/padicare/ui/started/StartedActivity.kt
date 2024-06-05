package com.capstone.padicare.ui.started

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityStartedBinding
import com.capstone.padicare.ui.login.LoginActivity
import com.capstone.padicare.ui.register.RegisterActivity
import androidx.core.util.Pair

class StartedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener{
            val intent = Intent(this@StartedActivity, LoginActivity::class.java)
            val optionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@StartedActivity,
                Pair(binding.ivImage, "logo"),
                Pair(binding.loginButton, "login"),
                Pair(binding.registerButton, "signup")
            )
            startActivity(intent, optionCompat.toBundle())
        }
        binding.registerButton.setOnClickListener{
            val intent = Intent(this@StartedActivity, RegisterActivity::class.java)
            val optionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@StartedActivity,
                Pair(binding.ivImage, "logo"),
                Pair(binding.loginButton, "login"),
                Pair(binding.registerButton, "signup")
            )
            startActivity(intent, optionCompat.toBundle())
        }
    }
}