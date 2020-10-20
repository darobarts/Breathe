package com.darobarts.breathe.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

sealed class ViewState {
    object Loading: ViewState()
    data class ExhaleStep(val secondsLeft: Int): ViewState()
    data class InhaleStep(val secondsLeft: Int): ViewState()
}

class MainViewModel : ViewModel() {

    companion object {
        private const val SECONDS_OF_EXHALE = 5
        private const val SECONDS_OF_INHALE = 5
    }

    val viewState: LiveData<ViewState>
    get() = _viewState

    private var _viewState = MutableLiveData<ViewState>()

    init {
        _viewState.value = ViewState.Loading
        setupTimer()
    }

    private fun setupTimer() {
        _viewState.value = ViewState.InhaleStep(SECONDS_OF_INHALE)
    }

}
