package com.capstone.padicare.ui.news

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentNewsBinding
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory

class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NewsViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        newsAdapter = NewsAdapter()
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }

        viewModel.fetchNews()

        viewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        sharedPreferences = requireActivity().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            viewModel.fetchUserInfo(token).observe(viewLifecycleOwner, Observer { result ->

                when (result) {
                    is ResultState.Success -> {
                        val user = result.data
                        Log.i("HomeFragment", "User Info: ${user.toString()}")
                        binding.tvHi.text = getString(R.string.hi, user.data.name)
                    }
                    is ResultState.Error -> {
                        if (result.error.contains("token is invalid/expired")) {
                            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.i("HomeFragment", "Error: ${result.error}")
                            Toast.makeText(requireContext(), "Failed to get user info: ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is ResultState.Loading -> {

                    }
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token is missing", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
