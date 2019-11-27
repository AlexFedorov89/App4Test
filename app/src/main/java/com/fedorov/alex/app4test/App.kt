package com.fedorov.alex.app4test

import android.app.Application
import android.content.SharedPreferences

const val APP_PREFERENCES = "settings"
const val APP_PREFERENCES_BLUETOOTH_ON = "bluetooth_on"
const val APP_PREFERENCES_SAMPLE_RATE = "sample_rate"

class App : Application() {

    lateinit var pref: SharedPreferences

    var sampleRate: Int
        get() = pref.getInt(APP_PREFERENCES_SAMPLE_RATE, 48_000)
        set(value) {
            val editor = pref.edit()
            editor.putInt(APP_PREFERENCES_SAMPLE_RATE, value)
            editor.apply()
        }

    var bluetoothOn: Boolean
        get() = pref.getBoolean(APP_PREFERENCES_BLUETOOTH_ON, false)
        set(value) {
            val editor = pref.edit()
            editor.putBoolean(APP_PREFERENCES_BLUETOOTH_ON, value)
            editor.apply()
        }

    override fun onCreate() {
        super.onCreate()

        pref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }
}