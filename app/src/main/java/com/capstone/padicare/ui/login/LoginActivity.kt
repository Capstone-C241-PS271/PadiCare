package com.capstone.padicare.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        setupView()
        setupAction()

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
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

    private fun observeLoginResponse() {
        loginViewModel.loginResponse.observe(this){ response ->
            if (response.error){
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this).apply {
                    setTitle("Yosh!")
                    setMessage(getString(R.string.loginSuccess))
                    setPositiveButton(getString(R.string.next)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                        showLoading(false)
                    }
                    create()
                    show()
                }
            }
        }
    }
}