package com.me.moviesapp.presentation

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext




@HiltAndroidApp
class MyApplication : Application(){

    private var instance: MyApplication? = null

    private fun MyApplication(): MyApplication? {
        instance = this
        return  instance
    }

    fun getInstance(): Context? {
        if (null == instance) {
            instance = MyApplication()
        }
        return instance
    }
}
