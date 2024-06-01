package com.capstone.padicare.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityLoginBinding
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Periksa status login
        checkLoginStatus()

        setupAction()
    }

    private fun setupAction() {
        binding.SignInButton.setOnClickListener {
            val email = binding.edLoginUsername.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edLoginUsername.error = getString(R.string.emailValid)
                }

                password.isEmpty() -> {
                    binding.edRegisterPassword.error = getString(R.string.passwordValid)
                }
                else -> {
                    loginViewModel.login(email, password)
                    showLoading(true)
                }
            }
        }
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        observeLoginResponse()
        observeLoadingState()
        observeErrorState()
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("StoryAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("StoryAppPreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun observeLoginResponse() {
        loginViewModel.loginResponse.observe(this) { response ->
            if (response.error) {
                showLoading(false)
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.login_failed_title))
                    setMessage(getString(R.string.login_failed_message))
                    setPositiveButton(getString(R.string.retry)) { _, _ -> }
                    create()
                    show()
                }
            } else {
                showLoading(false)
                saveLoginStatus(true) // Simpan status login saat berhasil login
                val intent = Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun observeErrorState() {
        loginViewModel.isError.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showError(errorMessage)
                showLoading(false)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun observeLoadingState() {
        loginViewModel.isLoading.observe(this){ isLoading ->
            showLoading(isLoading)
        }
    }
}
