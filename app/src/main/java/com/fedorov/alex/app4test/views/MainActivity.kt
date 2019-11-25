package com.fedorov.alex.app4test.views

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fedorov.alex.app4test.R
import com.fedorov.alex.app4test.viewModels.*
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), MainActivityNavigator {

    override fun openPreferenceActivity() {
        PreferenceActivity.start(this)
    }

    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val Int.px2dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(
            this,
            MyViewModelFactory(this, "$cacheDir/", "$filesDir/", application)
        ).get(MainViewModel::class.java)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            finish()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        timerView.text = viewModel.time()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        setSupportActionBar(bottomMain)
    }

    override fun onStart() {
        super.onStart()

        initViews()
    }

    private fun isCurrentModePortrait(): Boolean =
        resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    private fun initViews() {
        // Observe for timer text view.
        viewModel.timerText().observe(this, Observer<String> { timerText ->
            timerView.text = timerText
        })
        // Observe for message to UI.
        viewModel.msgToView().observe(this, Observer<String> { msg ->
            if (msg.isNotEmpty()) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })

        viewModel.recordingState.observe(this, Observer {
            when (it) {
                is NotRecording -> returnAppBarToDefault()
                is Recording -> prepareAppBarToRec()
                is OnPause -> {
                    animateBottomAppBar()
                    setFabImage(true)
                }
            }
        })

        fab.setOnClickListener {
            viewModel.startRecordFromUI()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)

        when (viewModel.recordingState.value) {
            is NotRecording -> {
                setVisibleMenuItemsDefault()
            }
            is Recording -> {
                setVisibleMenuItemsRecording()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_records -> {
                PlaylistActivity.start(this)
            }

            R.id.navigation_done -> {
                viewModel.saveRecordFromUI()

                returnAppBarToDefault()
            }

            android.R.id.home -> {
                viewModel.homeBtnFromUI()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun prepareAppBarToRec() {
        bottomMain.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_close)
        setVisibleMenuItemsRecording()
        setFabImage(false)
        // Make animation of update padding bottom app bar.
        animateBottomAppBar()
    }

    private fun setVisibleMenuItemsRecording() {
        bottomMain.menu.findItem(R.id.navigation_done)?.isVisible = true
        bottomMain.menu.findItem(R.id.navigation_records)?.isVisible = false
    }

    private fun setFabImage(start: Boolean) {
        if (start) {
            fab.setImageResource(R.drawable.ic_play)
        } else {
            fab.setImageResource(R.drawable.ic_pause)
        }
    }

    private fun returnAppBarToDefault() {
        timerView.text = getString(R.string.blankTime)
        setVisibleMenuItemsDefault()

        bottomMain.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_settings)
        // Change fab image to default.
        fab.setImageResource(0)
        // Make animation of update padding bottom app bar.
        animateBottomAppBar(true)
    }

    private fun setVisibleMenuItemsDefault() {
        bottomMain.menu.findItem(R.id.navigation_done)?.isVisible = false
        bottomMain.menu.findItem(R.id.navigation_records)?.isVisible = true
    }

    private fun animateBottomAppBar(toDefault: Boolean = false) {
        val pxFrom = bottomMain.paddingLeft
        val pxTo = if (toDefault) {
            resources.getInteger(R.integer.bottom_menu_left_padding_def).px2dp
        } else {
            (resources.getInteger(R.integer.bottom_menu_left_padding_rec_portrait).takeIf { isCurrentModePortrait() }
                ?: resources.getInteger(R.integer.bottom_menu_left_padding_rec_landscape)).px2dp
        }

        ValueAnimator.ofInt(pxFrom, pxTo).run {
            duration = resources.getInteger(R.integer.animation_duration).toLong()
            start()

            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                bottomMain.updatePadding(left = animatedValue, right = animatedValue)
            }
        }
    }
}