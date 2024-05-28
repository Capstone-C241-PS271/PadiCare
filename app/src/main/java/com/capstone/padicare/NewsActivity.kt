package com.capstone.padicare

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_news)

        initViews()
        initBottomNavigationView()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottomNav)
    }

    private fun initBottomNavigationView() {
        bottomNavigationView.selectedItemId = R.id.news
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.news -> {
                startActivity(Intent(this, NewsActivity::class.java))
                true
            }
            R.id.scan -> {
                startActivity(Intent(this, ScanActivity::class.java))
                true
            }
            R.id.history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            else -> false
        }
    }
}
    }