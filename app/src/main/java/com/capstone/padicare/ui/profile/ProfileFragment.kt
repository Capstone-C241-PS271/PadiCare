package com.capstone.padicare.ui.profile

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.capstone.padicare.R
import com.capstone.padicare.databinding.FragmentProfileBinding
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.started.StartedActivity

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentProfileBinding? = null
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

        viewModel.getData().observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.name
            binding.tvEmail.text = user.email
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
