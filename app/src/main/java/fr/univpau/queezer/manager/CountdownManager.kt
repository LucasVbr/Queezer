package fr.univpau.queezer.manager

import android.content.Context
import android.os.CountDownTimer

class CountdownManager (val duration: Long, val onTickTimer: () -> Unit, val onFinishTimer: () -> Unit) {

    var timeLeft = duration / 1000;
    var interval = 1000L;
    var timer: CountDownTimer? = null

    private fun create() {
        timer = object : CountDownTimer(duration, interval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished / 1000;
                onTickTimer()
            }
            override fun onFinish() {
                onFinishTimer()
            }
        }
    }

    fun start() {
        create()
        timer?.start()
    }

    fun stop() {
        timer?.cancel()
        timer = null
    }

    fun restart() {
        stop()
        start()
    }
}