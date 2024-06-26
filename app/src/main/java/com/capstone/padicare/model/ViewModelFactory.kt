package com.capstone.padicare.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.padicare.data.repo.UserRepository
import com.capstone.padicare.data.retrofit.Injection
import com.capstone.padicare.ui.feed.FeedViewModel
import com.capstone.padicare.ui.home.HomeViewModel
import com.capstone.padicare.ui.login.LoginViewModel
import com.capstone.padicare.ui.news.NewsViewModel
import com.capstone.padicare.ui.profile.ProfileViewModel
import com.capstone.padicare.ui.register.RegisterViewModel

class ViewModelFactory (private val repo: UserRepository): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <i : ViewModel> create(modelClass: Class<i>): i {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repo) as i
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repo) as i
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repo) as i
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repo) as i
            }
            modelClass.isAssignableFrom(NewsViewModel::class.java) -> {
                NewsViewModel(repo) as i
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repo) as i
            }
            modelClass.isAssignableFrom(FeedViewModel::class.java) -> {
                FeedViewModel(repo) as i
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class:"+modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory{
            if (INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}