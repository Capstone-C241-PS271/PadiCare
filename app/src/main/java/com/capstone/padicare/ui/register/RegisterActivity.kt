package com.capstone.padicare.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.capstone.padicare.databinding.ActivityRegisterBinding
import com.capstone.padicare.ui.login.LoginActivity
import androidx.core.app.ActivityOptionsCompat

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@RegisterActivity,
                Pair(binding.titleTextView, "title1"),
                Pair(binding.welcome, "title2"),
                Pair(binding.welcome2, "title3"),
                Pair(binding.emailEditTextLayout, "email"),
                Pair(binding.usernameEditTextLayout, "username"),
                Pair(binding.passwordEditTextLayout, "password"),
                Pair(binding.SignUpButton, "signup"),
                Pair(binding.tvLogin, "account1"),
                Pair(binding.btnLogin, "account2")
            )
            startActivity(intent, optionsCompat.toBundle())
        }
    }
}