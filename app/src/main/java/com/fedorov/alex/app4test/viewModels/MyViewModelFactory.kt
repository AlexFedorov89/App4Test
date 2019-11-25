package com.fedorov.alex.app4test.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.fedorov.alex.app4test.views.MainActivityNavigator

class MyViewModelFactory(val activity:MainActivityNavigator, val cacheDir:String, val filesDir:String, val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel("$cacheDir/", "$filesDir/", application, activity) as T
    }
}