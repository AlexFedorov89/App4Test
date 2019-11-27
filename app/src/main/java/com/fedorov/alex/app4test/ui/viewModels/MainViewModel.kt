package com.fedorov.alex.app4test.ui.viewModels

// TODO Move all androids import to another class.
//---------------------
import android.annotation.TargetApi
import android.app.Application
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
//---------------------

import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// TODO Think about this.
//---------------------
import com.fedorov.alex.app4test.App
//---------------------

import com.fedorov.alex.app4test.data.Repository

// TODO Get rid of this callback.
//---------------------
import com.fedorov.alex.app4test.ui.views.MainActivityNavigator
//---------------------
import com.fedorov.alex.app4test.utils.ITimer
import com.fedorov.alex.app4test.utils.TimerImpl
import java.io.IOException

class MainViewModel(
    private val cachePath: String, private val storagePath: String,
    application: Application,
    private val mainActivityNavigator: MainActivityNavigator
) : AndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName
    private val recordingPresenter = RecordingPresenter()
    private var recorder: MediaRecorder? = null

    private val timer: ITimer = TimerImpl(this)
    private val timerTextLiveData: MutableLiveData<String> = MutableLiveData()
    private val msgToView: MutableLiveData<String> = MutableLiveData()

    val recordingState: MutableLiveData<RecordingState> = MutableLiveData()

    fun time(): String = timer.time()
    fun timerText(): LiveData<String> = timerTextLiveData
    fun msgToView(): LiveData<String> = msgToView

    fun loadTimerText(v: String) {
        this.timerTextLiveData.postValue(v)
    }

    private fun startRecording() {
        recordingPresenter.startRecording()
        recordingState.value = recordingPresenter.state
        timer.startTimer()

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION)
            setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
            setOutputFile(cachePath + Repository.newRecord(storagePath))
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
            // get Sample rate from Shared Preferences.
            setAudioSamplingRate((getApplication<Application>().applicationContext as App).sampleRate)
            try {
                prepare()
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
            start()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setRecordingOnPause() {
        recordingPresenter.onPause()
        recordingState.value = recordingPresenter.state
        timer.pauseTimer()
        recorder?.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        recordingPresenter.startRecording()
        recordingState.value = recordingPresenter.state
        timer.resumeTimer()
        recorder?.resume()
    }

    private fun stopRecording(saveAudio: Boolean = false) {
        recordingPresenter.stopRecording()
        recordingState.value = recordingPresenter.state
        timer.stopTimer()

        recorder?.apply {
            stop()
            if (saveAudio) {
                release()
                Repository.saveRecord(cachePath, storagePath)
            } else {
                Repository.deleteRecord(cachePath)
            }
        }
        recorder = null
    }

    private fun sendMsgToUI(s: String) {
        msgToView.value = s
        msgToView.value = ""
    }

    fun startRecordFromUI() {
        if (recordingPresenter.isOnPause) {
            //sendMsgToUI("Stop pause and start recording")

            resumeRecording()
        } else {
            if (!recordingPresenter.isRecording) {
                //sendMsgToUI("Start recording")
                startRecording()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //sendMsgToUI("On pause")
                setRecordingOnPause()
            }
        }
    }

    fun saveRecordFromUI() {
        stopRecording(true)
    }

    fun homeBtnFromUI(showAskDialog: Boolean = true) {
        when (recordingPresenter.isRecording || recordingPresenter.isOnPause) {
            true -> {
                // cancel operation
                //sendMsgToUI("Cancel")
                if (showAskDialog) {
                    mainActivityNavigator.askUserRecordCancel()
                } else {
                    stopRecording(false)
                }
            }
            false -> {
                // open settings
                //sendMsgToUI("Open settings")
                mainActivityNavigator.openPreferenceActivity()
            }
        }
    }
}
