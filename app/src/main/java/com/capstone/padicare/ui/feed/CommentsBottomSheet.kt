package com.capstone.padicare.ui.feed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.capstone.padicare.R
import com.capstone.padicare.data.response.CommentRequest
import com.capstone.padicare.data.response.CommentResponse
import com.capstone.padicare.data.retrofit.ApiConfig
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CommentsBottomSheet(private val id: Int) : BottomSheetDialogFragment() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments_bottom_sheet, container, false)

        val commentEditText: EditText = view.findViewById(R.id.etNewComment)
        val sendButton: Button = view.findViewById(R.id.btnSubmitComment)

        sendButton.setOnClickListener {
            val content = commentEditText.text.toString()
            if (content.isNotEmpty()) {
                val comment = CommentRequest(id, content)
                postComment(comment)
            } else {
                Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun postComment(comment: CommentRequest) {
        val apiService = ApiConfig.getApiService()
        sharedPreferences = requireContext().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("token", "") ?: ""
        token = "Bearer $token"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<CommentResponse> = apiService.postComment(comment.id, comment, token)
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Comment posted successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(context, "Failed to post comment", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
