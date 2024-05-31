package com.capstone.padicare.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.capstone.padicare.R
import com.capstone.padicare.model.MainViewModel
import com.capstone.padicare.model.ViewModelFactory
import com.capstone.padicare.ui.login.LoginActivity


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity())).get(MainViewModel::class.java)

        val btnContact: Button = view.findViewById(R.id.btn_contact)
        btnContact.setOnClickListener(this)

        val btnAboutApp: Button = view.findViewById(R.id.btn_about)
        btnAboutApp.setOnClickListener(this)

        val btnLogout: Button = view.findViewById(R.id.buttonLogout)
        btnLogout.setOnClickListener(this)

    }
    override fun onClick(v: View?){
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
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}
