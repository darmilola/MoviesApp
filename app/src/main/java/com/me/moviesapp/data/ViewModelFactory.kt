package com.me.moviesapp.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.me.moviesapp.presentation.ViewModels.LatestViewModel
import com.me.moviesapp.presentation.ViewModels.PopularViewModel
import com.me.moviesapp.presentation.ViewModels.UpcomingViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LatestViewModel::class.java)) {
            return LatestViewModel(MainRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(PopularViewModel::class.java)) {
            return PopularViewModel(MainRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }



}