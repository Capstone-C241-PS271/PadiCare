package com.capstone.padicare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.padicare.databinding.ActivityMainBinding
import com.capstone.padicare.ui.history.HistoryFragment
import com.capstone.padicare.ui.home.HomeFragment
import com.capstone.padicare.ui.news.NewsFragment
import com.capstone.padicare.ui.profile.ProfileFragment
import com.capstone.padicare.ui.scan.ScanFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleDirectChangeFragment()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment(), true)
                R.id.news -> replaceFragment(NewsFragment(), true)
                R.id.scan -> replaceFragment(ScanFragment(), true)
                R.id.history -> replaceFragment(HistoryFragment(), true)
                R.id.profile -> replaceFragment(ProfileFragment(), true)
                else -> {}
            }
            true
        }
    }

    private  fun handleDirectChangeFragment() {
        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "HistoryFragment") {
            replaceFragment(HistoryFragment(), true)
            binding.bottomNav.selectedItemId = R.id.history
        } else if(navigateTo == "ScanFragment") {
            replaceFragment(ScanFragment(), true)
            binding.bottomNav.selectedItemId = R.id.scan
        } else {
            replaceFragment(HomeFragment(), true)
            binding.bottomNav.selectedItemId = R.id.home
        }
    }

    private fun replaceFragment(fragment: Fragment, showBottomNav: Boolean, addToBackStack: Boolean = false) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_container, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

        if (showBottomNav) {
            binding.bottomNav.visibility = android.view.View.VISIBLE
        } else {
            binding.bottomNav.visibility = android.view.View.GONE
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun openUrl(url: String) {
        url.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}