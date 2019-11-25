package com.fedorov.alex.app4test.viewModels

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fedorov.alex.app4test.data.Repository
import com.fedorov.alex.app4test.data.Repository.records
import com.fedorov.alex.app4test.data.model.Record
import com.fedorov.alex.app4test.views.MainViewState
import java.io.IOException

class PlaylistViewModel : ViewModel() {

    private val TAG = PlaylistViewModel::class.java.simpleName

    private val viewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    private var player: MediaPlayer? = null

    init {
        Repository.getRecords().observeForever {
            records.let {
                viewStateLiveData.value =
                    viewStateLiveData.value?.copy(records = it) ?: MainViewState(it)
            }
        }
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    fun playRecord(record: Record) {
        startPlaying(record.path + record.filename)
    }

    private fun startPlaying(fileName: String) {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                player?.setOnCompletionListener {
                    stopPlaying()
                }
            } catch (e: IOException) {
                Log.e(TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }
}