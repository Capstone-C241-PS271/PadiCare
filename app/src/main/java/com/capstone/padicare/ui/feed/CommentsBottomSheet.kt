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
import com.capstone.padicare.R
import com.capstone.padicare.data.response.CommentRequest
import com.capstone.padicare.data.response.CommentResponse
import com.capstone.padicare.data.retrofit.ApiConfig
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class CommentsBottomSheet(private val id: Int) : BottomSheetDialogFragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

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
}
