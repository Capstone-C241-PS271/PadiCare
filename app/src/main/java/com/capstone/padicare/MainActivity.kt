package com.capstone.padicare

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.capstone.padicare.databinding.ActivityMainBinding
import com.capstone.padicare.ui.CameraFragment
import com.capstone.padicare.ui.history.HistoryFragment
import com.capstone.padicare.ui.home.HomeFragment
import com.capstone.padicare.ui.news.NewsFragment
import com.capstone.padicare.ui.profile.ProfileFragment
import com.capstone.padicare.ui.scan.ScanFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var fragment: Fragment = ScanFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment(), true)

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

        // Monitor changes in the back stack
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fl_container)
            if (fragment is CameraFragment) {
                binding.bottomNav.visibility = android.view.View.GONE
            } else {
                binding.bottomNav.visibility = android.view.View.VISIBLE
            }
        }
    }

    fun navigateToCamera() {
        fragment = CameraFragment()
        replaceFragment(fragment, false, true)
    }

    private fun replaceFragment(fragment: Fragment, showBottomNav: Boolean, addToBackStack: Boolean = false) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_container, fragment)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()

        // Set visibility of BottomNavigationView
        if (showBottomNav) {
            binding.bottomNav.visibility = android.view.View.VISIBLE
        } else {
            binding.bottomNav.visibility = android.view.View.GONE
        }
    }

    fun showUpButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton(){
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun clickUpButton(){
        addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                return
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    android.R.id.home -> {
                        supportFragmentManager.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    fun openUrl(url: String) {
//        val url = view.getTag(R.id.news_link) as? String
        url.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }
}