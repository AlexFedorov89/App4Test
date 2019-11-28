package com.fedorov.alex.app4test.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class TimerImpl() : ITimer {
    private var timerText: MutableLiveData<String> = MutableLiveData()
    private lateinit var timerDate: Date

    private fun blankDate(): Date {
        return Calendar.getInstance().apply {
            set(0, 0, 0, 0, 0, 0)
        }.time
    }

    private var timer: Timer? = null
    private val timerDateFormat: DateFormat = SimpleDateFormat("HH:mm:ss")

    override fun time(): LiveData<String> = timerText

    private fun initTimer(cont: Boolean = false) {
        if (timer == null) {
            timer = Timer()
        }

        if (!cont) {
            timerDate = blankDate()
        }

        timer?.schedule(1000, 1000) {
            timerDate.time += 1000
            setTimerText(timerDate)
        }
    }

    private fun setTimerText(value: Date) {
        timerText.postValue(timerDateFormat.format(value).toString())
    }

    override fun startTimer() {
        initTimer()
    }

    override fun stopTimer() {
        pauseTimer()
        timerDate = blankDate()
        setTimerText(timerDate)
    }

    override fun pauseTimer() {
        timer?.cancel()
        timer = null
    }

    override fun resumeTimer() {
        initTimer(true)
    }
}