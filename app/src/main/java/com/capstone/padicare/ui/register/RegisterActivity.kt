package com.capstone.padicare.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.capstone.padicare.R
import com.capstone.padicare.databinding.ActivityRegisterBinding
import com.capstone.padicare.ui.login.LoginActivity
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignUpButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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

        setupAction()
    }

    private fun setupAction() {
        binding.SignUpButton.setOnClickListener{
            val name = binding.edLoginUsername.text?.toString() ?: ""
            val email = binding.edLoginEmail.text?.toString() ?: ""
            val password = binding.edRegisterPassword.text?.toString() ?: ""

            if (!validateInput(name, email, password)) return@setOnClickListener

            viewModel.register(name, email, password)
            viewModel.registrationResult.observe(this){ result ->
                showLoading(result is ResultState.Loading)

                when (result){
                    is ResultState.Success -> {
                        showLoading(false)
                        result.data.let { data ->
                            data.message?.let { message -> AlertDialog.Builder(this).apply {
                                setTitle("Selamat!")
                                setMessage("Akun dengan $email sudah jadi nih. Silahkan, Login.")
                                setPositiveButton("Next"){_,_ ->
                                    finish()
                                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                }
                                create()
                                show()
                            } }
                        }
                    }
                    is ResultState.Error -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Sorry..")
                            setMessage(result.error)
                            setPositiveButton("Try Again"){_,_, ->
                            }
                            create()
                            show()
                        }
                    }
                    is ResultState.Loading -> showLoading(true)
                }
            }
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

    private fun validateInput(email: String, password: String, password1: String): Boolean {
        var isValid = true

        if (email.isEmpty() && password.isEmpty()) {
            setError(binding.emailEditTextLayout, getString(R.string.error_empty_email))
            setError(binding.usernameEditTextLayout, getString(R.string.error_empty_username))
            setError(binding.passwordEditTextLayout, getString(R.string.error_empty_password))
            isValid = false
        } else {
            if (email.isEmpty()) {
                setError(binding.emailEditTextLayout, getString(R.string.error_empty_email))
                isValid = false
            } else {
                clearError(binding.emailEditTextLayout)
            }

            if (!isValidEmail(email)) {
                setError(binding.emailEditTextLayout, getString(R.string.error_invalid_email))
                isValid = false
            } else {
                clearError(binding.emailEditTextLayout)
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


    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}