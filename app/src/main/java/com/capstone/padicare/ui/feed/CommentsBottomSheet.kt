package com.capstone.padicare.ui.feed

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.padicare.R
import com.capstone.padicare.data.response.CommentRequest
import com.capstone.padicare.data.response.CommentResponse
import com.capstone.padicare.data.response.DataItem
import com.capstone.padicare.data.retrofit.ApiConfig
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class CommentsBottomSheet(private val id: Int) : BottomSheetDialogFragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val commentsList: List<DataItem> = mutableListOf()
    private lateinit var commentsAdapter: CommentsAdapter


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments_bottom_sheet, container, false)

        val commentEditText: EditText = view.findViewById(R.id.etNewComment)
        val sendButton: Button = view.findViewById(R.id.btnSubmitComment)
        val commentsRecyclerView: RecyclerView = view.findViewById(R.id.rvComments)

        commentsAdapter = CommentsAdapter(commentsList)
        commentsRecyclerView.layoutManager = LinearLayoutManager(context)
        commentsRecyclerView.adapter = commentsAdapter

        sendButton.setOnClickListener {
            val content = commentEditText.text.toString()
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
    }

    private fun postComment(comment: CommentRequest, postId: Int) {
        val apiService = ApiConfig.getApiService()
        sharedPreferences = requireContext().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("token", "") ?: ""
        token = "Bearer $token"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response: Response<CommentResponse> = apiService.postComment(postId, comment, token)
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

    private suspend fun getComment(){
        val apiService = ApiConfig.getApiService()
        sharedPreferences = requireActivity().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("token", "")?:""

        try {
            val response = withContext(Dispatchers.IO){
                apiService.getPosts("Bearer $token")
            }
            if (response.isSuccessful){
                val commentResponse = response.body()
                val commentList = commentResponse?.data?.reversed()
                commentsAdapter = CommentsAdapter(commentList)

            } else{
                Log.e("FeedActivity", "Failed to fetch posts: ${response.code()}")
            }
        } catch (e: Exception){
            Log.e("FeedActivity", "Error: ${e.message}")
        }
    }
}
