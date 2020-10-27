package com.darobarts.breathe.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.darobarts.breathe.databinding.MainFragmentBinding

class ExerciseFragment : Fragment() {

    companion object {
        fun newInstance() = ExerciseFragment()
    }

    private var switchBool: Boolean = true
    private var animationInProgress = false

    private lateinit var viewModel: ExerciseViewModel

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)
        setupObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.testButton.setOnClickListener {
            //TODO
        }
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

    private fun showInhaleStep(inhaleStep: ViewState.InhaleStep) = with(binding) {
        hideLoadingState()
        circularCounter.bindView(90, inhaleStep.millisecondsToInhale)
        circularCounter.visibility = View.VISIBLE
        circularCounter.setOnClickListener {
            if (!animationInProgress) {
                animationInProgress = true
                if (switchBool) {
                    circularCounter.shrinkCircle(
                        90,
                        50,
                        inhaleStep.millisecondsToInhale
                    ) { animationInProgress = false }
                } else {
                    circularCounter.growCircle(
                        50,
                        90,
                        inhaleStep.millisecondsToInhale
                    ) { animationInProgress = false }
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
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoadingState() = with(binding) {
        progressBar.visibility = View.VISIBLE
        circularCounter.visibility = View.GONE
        progressBar.isIndeterminate = true
    }
}
