package com.fedorov.alex.app4test.utils

import com.fedorov.alex.app4test.ui.viewModels.MainViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class TimerImpl(private val viewModel: MainViewModel) : ITimer {
    private fun blankDate():Date {
        return Calendar.getInstance().apply {
            set(0, 0, 0, 0, 0, 0)
        }.time
    }

    private var timerDate = blankDate()

    private var timer: Timer? = null
    private val timerDateFormat: DateFormat = SimpleDateFormat("HH:mm:ss")

    override fun time() = timerDateFormat.format(timerDate).toString()

    private fun initTimer(cont: Boolean = false) {
        if (timer == null) {
            timer = Timer()
        }

        if (!cont) {
            timerDate = blankDate()
        }

        timer?.schedule(1000, 1000) {
            timerDate.time += 1000

            viewModel.loadTimerText(time())
        }
    }

    override fun startTimer() {
        initTimer()
    }

    override fun stopTimer() {
        pauseTimer()
        timerDate = blankDate()
    }

    override fun pauseTimer() {
        timer?.cancel()
        timer = null
    }

    override fun resumeTimer() {
        initTimer(true)
    }
}