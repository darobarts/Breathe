package com.darobarts.breathe.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.darobarts.breathe.R
import com.darobarts.breathe.databinding.MainFragmentBinding
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var timer: CountDownTimer? = null
    private var switchBool: Boolean = true
    private var animationInProgress = false

    private lateinit var viewModel: MainViewModel

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupObservers()
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.testButton.setOnClickListener {
            setupTimer()
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

    private fun setupTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(TimeUnit.SECONDS.toMillis(5), 50) {
            override fun onTick(millisUntilFinished: Long) {
                binding.countdown.text = getCountdownString(millisUntilFinished)
            }

            override fun onFinish() {
                binding.countdown.text = getCountdownString(0)
                viewModel.onStepFinished()
            }
        }.start()
    }

    private fun getCountdownString(msRemaining: Long): String {
        return String.format("%1.1f", msRemaining.toDouble() / 1000)
    }

    private fun showInhaleStep(inhaleStep: ViewState.InhaleStep) = with(binding) {
        hideLoadingState()
        circularCounter.bindView(90, inhaleStep.millisecondsToInhale)
        circularCounter.visibility = View.VISIBLE
        circularCounter.setOnClickListener {
            setupTimer()
            if (!animationInProgress) {
                animationInProgress = true
                if (switchBool) {
                    circularCounter.shrinkCircle(90, 50, inhaleStep.millisecondsToInhale) { animationInProgress = false }
                } else {
                    circularCounter.growCircle(50, 90, inhaleStep.millisecondsToInhale) { animationInProgress = false }
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

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
    }

}
