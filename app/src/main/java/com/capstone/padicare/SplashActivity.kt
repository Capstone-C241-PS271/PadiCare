package com.capstone.padicare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.capstone.padicare.ui.started.StartedActivity
import com.capstone.padicare.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginStatus()
        }, 2000L)
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            goToMainActivity()
        } else {
            goToStartedActivity()
        }
    }

    private fun goToStartedActivity() {
        Intent(this, StartedActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }

    private fun goToMainActivity() {
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
            finish()
        }
    }
}