package com.capstone.padicare.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.data.pref.UserPreference
import com.capstone.padicare.databinding.ActivityRegisterBinding
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        binding.SignUpButton.setOnClickListener {
            val name = binding.usernameEditTextLayout.editText?.text.toString().trim()
            val email = binding.emailEditTextLayout.editText?.text.toString().trim()
            val password = binding.passwordEditTextLayout.editText?.text.toString().trim()
            registerViewModel.registerUser(name, email, password)
        }

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

    private fun setupObservers() {
        registerViewModel.registerResult.observe(this, Observer { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    binding.progressBar.visibility = android.view.View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                }
                is ResultState.Error -> {
                    binding.progressBar.visibility = android.view.View.GONE
                    Toast.makeText(this, resultState.error, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
