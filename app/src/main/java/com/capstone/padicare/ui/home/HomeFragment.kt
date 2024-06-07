package com.capstone.padicare.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentHomeBinding
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.ui.login.LoginActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        sharedPreferences = requireActivity().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            viewModel.fetchUserInfo(token).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is ResultState.Success -> {
                        val user = result.data
                        binding.tvHi.text = getString(R.string.hi, user.name)
                    }
                    is ResultState.Error -> {
                        if (result.error.contains("token is invalid/expired")) {
                            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
                            redirectToLogin()
                        } else {
                            Toast.makeText(requireContext(), "Failed to get user info: ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is ResultState.Loading -> {
                        // Show loading indicator if needed
                        // binding.loadingIndicator.visibility = View.VISIBLE // Example
                    }
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token is missing", Toast.LENGTH_SHORT).show()
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        // Clear token
        sharedPreferences.edit().remove("token").apply()
        // Redirect to LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
