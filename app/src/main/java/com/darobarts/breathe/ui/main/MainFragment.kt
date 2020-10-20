package com.darobarts.breathe.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.darobarts.breathe.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var switchBool: Boolean = true
    private var animationInProgress = false

    private lateinit var viewModel: MainViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var circularCounterView: CircularCounterView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findViews(view)
    }

    private fun findViews(view: View) {
        progressBar = view.findViewById(R.id.progressBar)
        circularCounterView = view.findViewById(R.id.circularCounter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
        hideLoadingState()
        circularCounterView.visibility = View.VISIBLE
        circularCounterView.setOnClickListener {
            if (!animationInProgress) {
                animationInProgress = true
                if (switchBool) {
                    circularCounterView.shrinkCircle(90, 50, 5000) { animationInProgress = false }
                } else {
                    circularCounterView.growCircle(50, 90, 5000) { animationInProgress = false }
                }
                switchBool = !switchBool
            }
        }
    }

    private fun showExhaleStep(exhaleStep: ViewState.ExhaleStep) {
        hideLoadingState()
        TODO("Not yet implemented")
    }

    private fun hideLoadingState() {
        progressBar.visibility = View.GONE
    }

    private fun showLoadingState() {
        progressBar.visibility = View.VISIBLE
        circularCounterView.visibility = View.GONE
        progressBar.isIndeterminate = true
    }

}
