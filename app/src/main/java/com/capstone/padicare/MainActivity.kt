package com.capstone.padicare

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
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
        replaceFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.news -> replaceFragment(NewsFragment())
                R.id.scan -> replaceFragment(ScanFragment())
                R.id.history -> replaceFragment(HistoryFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_container, fragment)
        fragmentTransaction.commit()
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
}