package com.capstone.padicare.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        val buttonContact = binding?.btnContact
        buttonContact?.setOnClickListener(this)

        val buttonAbout = binding?.btnAbout
        buttonAbout?.setOnClickListener(this)
    }

    private fun setupView() {
        binding?.apply {
            btnBahasa.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_contact -> {
                findNavController().navigate(R.id.action_profileFragment_to_contactFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }
}
