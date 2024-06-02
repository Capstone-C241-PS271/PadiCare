package com.capstone.padicare.ui

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.capstone.padicare.databinding.FragmentCameraBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraFragment : Fragment() {
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private var imageCapture: ImageCapture? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestCameraPermission()
        }

        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener { switchCamera() }
    }

    private fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera()
    }

    private fun takePhoto() {
        val imageCaptureInstance = imageCapture ?: return

        val photoFile = File(
            requireContext().externalMediaDirs.firstOrNull(),
            "${SimpleDateFormat(FILENAME, Locale.US).format(System.currentTimeMillis())}.jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCaptureInstance.takePicture(
            outputOptions, ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    Log.d("CameraFragment", "Photo saved: $savedUri")
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Failed to take photo", exception)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder().build()

            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun requestCameraPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQ_CAMERA_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                startCamera()
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQ_CAMERA_PERMISSION = 100
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
    }
}
