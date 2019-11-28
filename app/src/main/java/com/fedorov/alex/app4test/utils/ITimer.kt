package com.fedorov.alex.app4test.utils

import androidx.lifecycle.LiveData

interface ITimer {
    fun startTimer()
    fun stopTimer()
    fun pauseTimer()
    fun resumeTimer()
    fun time(): LiveData<String>
}