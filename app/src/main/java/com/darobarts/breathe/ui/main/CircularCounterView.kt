package com.darobarts.breathe.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.TimeUtils
import android.view.View
import com.darobarts.breathe.R
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.min

class CircularCounterView : View {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    companion object {
        private const val REFRESH_MS: Long = 16
        private const val TIMER_DELAY: Long = 0
        private const val TIMER_SECOND_MS: Long = 1000
    }

    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private var radiusSizeFactor: Double = 1.0
    private var durationRemaining: Long = 0

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        color = resources.getColor(R.color.design_default_color_primary_dark)
    }

    private val textPaint = Paint().apply {
        color = resources.getColor(R.color.white)
        textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        centerX = w /2f
        centerY = h /2f
        updateRadius()
    }

    fun bindView(circleWidthPercentOfScreen: Int, duration: Long) {
        radiusSizeFactor = circleWidthPercentOfScreen.toDouble() / 100
        durationRemaining = duration
        invalidate()
    }

    fun shrinkCircle(factorFrom: Int, factorTo: Int, duration: Long, onCompletion: (() -> Unit)? = null) {
        require(factorFrom > factorTo)
        durationRemaining = duration
        val task = getShrinkTimer(factorFrom, factorTo, duration, onCompletion)
        schedulerWithTimer(task)
    }

    fun growCircle(factorFrom: Int, factorTo: Int, duration: Long, onCompletion: (() -> Unit)? = null) {
        require(factorFrom < factorTo)
        durationRemaining = duration
        val task = getGrowTimer(factorFrom, factorTo, duration, onCompletion)
        schedulerWithTimer(task)
    }

    private fun schedulerWithTimer(timerTask: TimerTask) {
        val timer = Timer()
        timer.scheduleAtFixedRate(timerTask, TIMER_DELAY, REFRESH_MS)
        timer.scheduleAtFixedRate(getCountdownTimerTask(), TIMER_DELAY, TIMER_SECOND_MS)
    }

    private fun getCountdownTimerTask() = object: TimerTask() {
        override fun run() {
            durationRemaining -= TIMER_SECOND_MS
            invalidateOnUiThread()
            if (durationRemaining == 0L) {
                cancel()
            }
        }
    }

    private fun getShrinkTimer(factorFrom: Int, factorTo: Int, duration: Long, onCompletion: (() -> Unit)? = null) = object : TimerTask() {
        private var index = factorFrom.toDouble()
        override fun run() {
            radiusSizeFactor = index / 100
            updateRadius()
            invalidateOnUiThread()
            index -= calculateSizeChangeDuration(abs(factorFrom - factorTo), duration)
            if (index <= factorTo) {
                cancel()
                onCompletion?.invoke()
            }
        }
    }

    private fun invalidateOnUiThread() {
        post {
            invalidate()
        }
    }

    private fun calculateSizeChangeDuration(growthDiff: Int, duration: Long): Double {
        val numIterationsNeeded = duration.toDouble() / growthDiff
        return growthDiff.toDouble() / numIterationsNeeded
    }

    private fun getGrowTimer(factorFrom: Int, factorTo: Int, duration: Long, onCompletion: (() -> Unit)? = null) = object : TimerTask() {
        private var index = factorFrom.toDouble()
        override fun run() {
            radiusSizeFactor = index / 100
            updateRadius()
            invalidateOnUiThread()
            index += calculateSizeChangeDuration(abs(factorFrom - factorTo), duration)
            if (index >= factorTo) {
                cancel()
                onCompletion?.invoke()
            }
        }
    }

    private fun updateRadius() {
        radius = getRadiusUsingFactor(getDefaultRadius(), radiusSizeFactor)
    }

    private fun getDefaultRadius(): Float {
        return (min(width, height) / 2).toFloat()
    }

    private fun getRadiusUsingFactor(radius: Float, factor: Double) : Float {
        return (radius * factor).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        textPaint.apply {
            textSize = centerX / 2
            isFakeBoldText = true
        }
        canvas?.drawCircle(centerX, centerY, radius, paint)
        canvas?.drawText("${durationRemaining}s", centerX, centerY, textPaint)
    }
}
