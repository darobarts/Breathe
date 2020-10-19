package com.darobarts.breathe.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.darobarts.breathe.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.viewState.observe(this, viewStateObserver)
    }

    private val viewStateObserver = Observer<ViewState> {
        when (it) {
            ViewState.Loading -> showLoadingState()
            is ViewState.ExhaleStep -> showExhaleStep(it)
            is ViewState.InhaleStep -> showInhaleStep(it)
        }
    }

    private fun showInhaleStep(inhaleStep: ViewState.InhaleStep) {
        TODO("Not yet implemented")
    }

    private fun showExhaleStep(exhaleStep: ViewState.ExhaleStep) {
        TODO("Not yet implemented")
    }

    private fun showLoadingState() {
        TODO("Not yet implemented")
    }

}
