package com.darobarts.breathe.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.concurrent.TimeUnit

sealed class ViewState {
    object Loading: ViewState()
    data class ExhaleStep(val secondsLeft: Int): ViewState()
    data class InhaleStep(val millisecondsToInhale: Long): ViewState()
}

class ExerciseViewModel : ViewModel() {

    companion object {
        private const val SECONDS_OF_EXHALE: Long = 5
        private const val SECONDS_OF_INHALE: Long = 5
    }

    val viewState: LiveData<ViewState>
    get() = _viewState

    private var _viewState = MutableLiveData<ViewState>()

    init {
        _viewState.value = ViewState.Loading
        setupTimer()
    }

    fun onStepFinished() {
        //TODO
    }

    private fun setupTimer() {
        _viewState.value = ViewState.InhaleStep(TimeUnit.SECONDS.toMillis(SECONDS_OF_INHALE))
    }

}
