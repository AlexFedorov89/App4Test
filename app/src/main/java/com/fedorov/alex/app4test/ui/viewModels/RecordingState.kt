package com.fedorov.alex.app4test.ui.viewModels

sealed class RecordingState
object NotRecording : RecordingState()
object Recording : RecordingState()
object OnPause : RecordingState()

class RecordingPresenter {
    var state: RecordingState =
        NotRecording
        private set

    val isRecording: Boolean
        get() = when (state) {
            is Recording -> true
            else -> false
        }

    val isOnPause: Boolean
        get() =
            when (state) {
                is OnPause -> true
                else -> false
            }

    fun startRecording() {
        state = Recording
    }

    fun onPause() {
        state = OnPause
    }

    fun stopRecording() {
        state = NotRecording
    }
}




