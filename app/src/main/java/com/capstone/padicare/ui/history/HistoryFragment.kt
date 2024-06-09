package com.capstone.padicare.ui.history

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.padicare.R
import com.capstone.padicare.data.response.Data
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        recyclerView = view.findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            fetchHistory()
        }
    }

    private suspend fun fetchHistory() {
        val apiService = ApiConfig.getApiService()
        sharedPreferences = requireContext().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("token", "") ?: ""

        try {
            val response = withContext(Dispatchers.IO) {
                apiService.getHistory("Bearer $token")
            }
            if (response.isSuccessful) {
                val historyResponse = response.body()
                val historyList = historyResponse?.data ?: emptyList()
                historyAdapter = HistoryAdapter(historyList)
                recyclerView.adapter = historyAdapter
        } else {
            Log.e("HistoryFragment", "Error: ${response.code()}")
        }
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Exception: ${e.message}", e)
        }
    }
}
