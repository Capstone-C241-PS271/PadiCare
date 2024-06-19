package com.capstone.padicare.ui.scan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentScanBinding
import com.capstone.padicare.ui.camera.CameraActivity
import com.capstone.padicare.data.response.PredictRequest
import com.capstone.padicare.data.retrofit.ApiConfig
import com.capstone.padicare.ui.result.ResultActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private val pickImageGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.previewImageView.setImageURI(uri)
            binding.analyzeButton.visibility = View.VISIBLE
        }
    }

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE ||
                        permissionName == Manifest.permission.READ_MEDIA_IMAGES) {
                        pickImageFromGallery()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission denied: $permissionName", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openCameraButton.setOnClickListener {
            openCameraActivity()
        }

        binding.galleryButton.setOnClickListener {
            handleGalleryButtonClicked()
        }

        binding.analyzeButton.setOnClickListener {
            val drawable = binding.previewImageView.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap
                CoroutineScope(Dispatchers.Main).launch {
                    uploadImage(bitmap)
                }
            } else {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Error")
                    .setMessage(getString(R.string.select_image))
                    .setPositiveButton("OK", null)
                    .create()
                alertDialog.show()
            }
        }

        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        binding.previewImageView.post {
            val width = binding.previewImageView.width
            val layoutParams = binding.previewImageView.layoutParams
            layoutParams.height = width
            binding.previewImageView.layoutParams = layoutParams
        }
    }

    override fun onResume() {
        super.onResume()
        loadCapturedImage()
    }

    private fun openCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun handleGalleryButtonClicked() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionsIfNeeded(arrayOf(Manifest.permission.READ_MEDIA_IMAGES));
        } else
            requestPermissionsIfNeeded(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    private fun requestPermissionsIfNeeded(permissions: Array<String>) {
        val permissionsToRequest = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionsLauncher.launch(permissionsToRequest)
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        pickImageGallery.launch("image/*")
    }

    private fun loadCapturedImage() {
        val sharedPreferences = requireContext().getSharedPreferences("com.capstone.padicare.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
        val imageUriString = sharedPreferences.getString("captured_image_uri", null)

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.previewImageView.setImageURI(imageUri)

            with(sharedPreferences.edit()) {
                remove("captured_image_uri")
                apply()
            }
        }
    }

    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private suspend fun uploadImage(bitmap: Bitmap) {
        val base64Image = convertBitmapToBase64(bitmap)
        sharedPreferences = requireContext().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        var token = sharedPreferences.getString("token", "") ?: ""
        token = "Bearer $token"

        val predictRequest = PredictRequest(image = base64Image)

        val progressBar = requireView().findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        try {
            val response = withContext(Dispatchers.IO) {
                ApiConfig.getApiService().predict(token, predictRequest)
            }

            if (response.isSuccessful) {
                val predictResponse = response.body()
                val predictionResult = predictResponse?.data?.result ?: "Unknown"
                val suggestion = predictResponse?.data?.suggestion ?: "No suggestion available"
                val imageUri = predictResponse?.data?.image

                Log.d("ScanFragment", "Prediction Result: $predictionResult")
                Log.d("ScanFragment", "Suggestion: $suggestion")

                val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                    putExtra("imageUri", imageUri.toString())
                    putExtra("predictionResult", predictionResult)
                    putExtra("suggestion", suggestion)
                }
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                Log.e("ScanFragment", "Upload failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show()
            Log.e("ScanFragment", "An error occurred: ${e.message}")
        } finally {
            progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
