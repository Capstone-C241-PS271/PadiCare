package com.capstone.padicare.ui.feed

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.Resource
import com.capstone.padicare.R
import com.capstone.padicare.data.response.CommentRequest
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.data.retrofit.ApiService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class CommentsBottomSheet(private val id: Int) : BottomSheetDialogFragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var commentAdapter: CommentsAdapter
    private val apiService = ApiConfig.getApiService()
    private lateinit var input: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments_bottom_sheet, container, false)

        input = view.findViewById(R.id.etNewComment)
        val sendButton: Button = view.findViewById(R.id.btnSubmitComment)

        sendButton.setOnClickListener {
            val content = input.text.toString()
            if (content.isNotEmpty()) {
                val comment = CommentRequest(content)
                postComment(comment, id)
            } else {
                Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        val layout = dialog.findViewById<CoordinatorLayout>(R.id.commentLayout)
        layout?.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        sharedPreferences =
            requireContext().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)

        loadComments()
    }

    private fun loadComments() {
        var token = sharedPreferences.getString("token", "") ?: ""
        token = "Bearer $token"

        commentAdapter = CommentsAdapter(arrayListOf())
        val container = dialog.findViewById<RecyclerView>(R.id.rvComments)
        container?.adapter = commentAdapter
        container?.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launch {
            try {
                val comments = apiService.getComments(id, token).await()
                commentAdapter.dispatch(comments.data.toCollection(ArrayList()))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun postComment(comment: CommentRequest, postId: Int) {
        val apiService = ApiConfig.getApiService()
        var token = sharedPreferences.getString("token", "") ?: ""
        token = "Bearer $token"

        apiService.postComment(postId, comment, token).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Comment posted successfully", Toast.LENGTH_SHORT)
                            .show()
                        input.text.clear()
                        loadComments()
                    } else {
                        Toast.makeText(context, "Failed to post comment", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity?.runOnUiThread {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}