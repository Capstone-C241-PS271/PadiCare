package com.capstone.padicare.ui.scan

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentScanBinding
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    private val pickImageGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            binding.previewImageView.setImageURI(uri)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            pickImageFromGallery()
        } else {
            // Permission denied, handle accordingly
        }
    }

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Register ActivityResult handler
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    // Handle the granted permission
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE ||
                        permissionName == Manifest.permission.READ_MEDIA_IMAGES) {
                        pickImageFromGallery()
                    }
                } else {
                    // Handle the denied permission
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
            (activity as MainActivity).navigateToCamera()
        }

        binding.galleryButton.setOnClickListener {
            handleGalleryButtonClicked()
        }

        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    private fun handleGalleryButtonClicked() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // API level 34 and above
                requestPermissionsIfNeeded(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    )
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // API level 33 (TIRAMISU)
                requestPermissionsIfNeeded(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )
                )
            }
            else -> {
                // API level below 33
                requestPermissionsIfNeeded(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
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

    private fun navigateToCamera(view: View) {
        binding.openCameraButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_scanFragment_to_cameraFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            // Handle permission denial if necessary
        }
    }

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1
    }
}
