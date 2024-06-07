package com.capstone.padicare.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.data.response.LoginResponse
import com.capstone.padicare.databinding.ActivityLoginBinding
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.register.RegisterActivity
import com.google.android.material.textfield.TextInputLayout
import androidx.core.util.Pair

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

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            val optionCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@LoginActivity,
                Pair(binding.titleTextView, "title1"),
                Pair(binding.welcome, "title2"),
                Pair(binding.welcome2, "title3"),
                Pair(binding.usernameEditTextLayout, "username"),
                Pair(binding.passwordEditTextLayout, "password"),
                Pair(binding.SignInButton, "signin")
            )
            startActivity(intent, optionCompat.toBundle())
        }
    }

    private fun setupAction() {
        binding.SignInButton.setOnClickListener {
            val email = binding.edLoginUsername.text.toString()
            val password = binding.edLoginPassword.text.toString()

            // Validasi input
            if (!validateInput(email, password)) return@setOnClickListener

            loginViewModel.login(email, password)
            observeLoginResponse()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty() && password.isEmpty()) {
            setError(binding.usernameEditTextLayout, getString(R.string.error_empty_email))
            setError(binding.passwordEditTextLayout, getString(R.string.error_empty_password))
            isValid = false
        } else {
            if (email.isEmpty()) {
                setError(binding.usernameEditTextLayout, getString(R.string.error_empty_email))
                isValid = false
            } else {
                clearError(binding.usernameEditTextLayout)
            }

            if (!isValidEmail(email)) {
                setError(binding.usernameEditTextLayout, getString(R.string.error_invalid_email))
                isValid = false
            } else {
                clearError(binding.usernameEditTextLayout)
            }

            if (password.isEmpty()) {
                setError(binding.passwordEditTextLayout, getString(R.string.error_empty_password))
                isValid = false
            } else {
                clearError(binding.passwordEditTextLayout)
            }

            if (password.length < 8) {
                setError(binding.passwordEditTextLayout, getString(R.string.error_short_password))
                isValid = false
            } else {
                clearError(binding.passwordEditTextLayout)
            }
        }

        return isValid
    }

    private fun observeLoginResponse() {
        loginViewModel.loginResponse.observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)
                    handleLoginSuccess(result.data)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showLoginErrorDialog()
                }
            }
        }
    }

    private fun handleLoginSuccess(response: LoginResponse) {
        saveLoginStatus(true)
        saveToken(response.token) // Simpan token setelah login berhasil
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showLoginErrorDialog() {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.login_failed_title))
            setMessage(getString(R.string.login_failed_message))
            setPositiveButton(getString(R.string.retry)) { _, _ -> }
            create()
            show()
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setError(textInputLayout: TextInputLayout, error: String) {
        textInputLayout.error = error
        if (textInputLayout == binding.passwordEditTextLayout) {
            textInputLayout.errorIconDrawable = null // Menyembunyikan icon error pada password
        }
    }

    private fun clearError(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
        textInputLayout.errorIconDrawable = null // Hapus icon kesalahan
    }

    private fun isValidEmail(email: String): Boolean {
        val atIndex = email.indexOf('@')
        val dotIndex = email.lastIndexOf('.')

        return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length - 1
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.INVISIBLE
        }
    }
}
