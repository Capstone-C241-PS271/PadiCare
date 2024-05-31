package com.capstone.padicare.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.capstone.padicare.MainActivity
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

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
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
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
}
