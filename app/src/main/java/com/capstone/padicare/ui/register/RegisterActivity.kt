package com.capstone.padicare.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import androidx.core.app.ActivityOptionsCompat
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory

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

        setupView()
        setupAction()
    }

    private fun setupAction() {
        binding.SignUpButton.setOnClickListener{
            val name = binding.edLoginUsername.text?.toString() ?: ""
            val email = binding.edLoginEmail.text?.toString() ?: ""
            val password = binding.edRegisterPassword.text?.toString() ?: ""

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
                            setPositiveButton("Try Again"){_,_ ->
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

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
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
