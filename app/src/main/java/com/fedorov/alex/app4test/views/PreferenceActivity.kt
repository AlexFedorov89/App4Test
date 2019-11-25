package com.fedorov.alex.app4test.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fedorov.alex.app4test.App
import com.fedorov.alex.app4test.R
import kotlinx.android.synthetic.main.activity_preferences.*


class PreferenceActivity : AppCompatActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PreferenceActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        initView()
    }

    private fun initView() {
        val lRadioButtons = mapOf(
            rb8kHz.id to 8_000,
            rb16kHz.id to 16_000,
            rb32kHz.id to 32_000,
            rb48kHz.id to 48_000
        )

        setButtonsValues(lRadioButtons)

        switchBluetoothRecord.setOnCheckedChangeListener { _, isChecked ->
            (application as App).bluetoothOn = isChecked
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            (application as App).sampleRate = lRadioButtons.get(checkedId) ?: 48_000
        }
    }

    private fun setButtonsValues(listRadioButtons: Map<Int, Int>) {
        // Set switch bluetooth button value from shared preferences.
        switchBluetoothRecord.setChecked((application as App).bluetoothOn)

        // Set radio button sample rate button value from shared preferences.
        listRadioButtons.filterValues { it == (application as App).sampleRate }.map {
            radioGroup.check(it.key)
        }
    }
}