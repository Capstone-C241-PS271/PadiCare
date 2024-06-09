package com.capstone.padicare.ui.profile

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentProfileBinding
import com.capstone.padicare.helper.ResultState
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.started.StartedActivity

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContact: Button = view.findViewById(R.id.btn_contact)
        btnContact.setOnClickListener(this)

        val btnAboutApp: Button = view.findViewById(R.id.btn_about)
        btnAboutApp.setOnClickListener(this)

        val btnLogout: Button = view.findViewById(R.id.buttonLogout)
        btnLogout.setOnClickListener(this)

        sharedPreferences = requireActivity().getSharedPreferences("PadiCarePreferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""

        if (token.isNotEmpty()) {
            viewModel.fetchUserInfo(token).observe(viewLifecycleOwner, Observer { result ->

                when (result) {
                    is ResultState.Success -> {
                        val user = result.data
                        Log.i("HomeFragment", "User Info: ${user.toString()}")
                        binding.tvUsername.text = user.data.name
                        binding.tvEmail.text = user.data.email
                    }
                    is ResultState.Error -> {
                        if (result.error.contains("token is invalid/expired")) {
                            Toast.makeText(requireContext(), "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.i("HomeFragment", "Error: ${result.error}")
                            Toast.makeText(requireContext(), "Failed to get user info: ${result.error}", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is ResultState.Loading -> {
                        // Show loading indicator if needed
                        // binding.loadingIndicator.visibility = View.VISIBLE // Example
                    }
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token is missing", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        val fragmentManager = parentFragmentManager
        when (v?.id) {
            R.id.btn_contact -> {
                val contactUsFragment = ContactUsFragment()
                fragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, contactUsFragment, ContactUsFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.btn_about -> {
                val aboutAppFragment = AboutAppFragment()
                fragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, aboutAppFragment, AboutAppFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
            R.id.buttonLogout -> {
                viewModel.logout()
                clearLoginStatus() // Hapus status login
                navigateToStartedActivity()
            }
        }
    }

    private fun clearLoginStatus() {
        // Bersihkan cache status login
        val sharedPreferences = requireContext().getSharedPreferences("PadiCarePreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("isLoggedIn")
        editor.apply()
    }

    private fun navigateToStartedActivity() {
        val intent = Intent(requireActivity(), StartedActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
