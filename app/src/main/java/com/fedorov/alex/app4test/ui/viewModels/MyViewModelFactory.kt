package com.fedorov.alex.app4test.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fedorov.alex.app4test.ui.views.MainActivityNavigator

class MyViewModelFactory(
    private val activity: MainActivityNavigator,
    private val cacheDir: String,
    private val filesDir: String,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(cacheDir, filesDir, application, activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}