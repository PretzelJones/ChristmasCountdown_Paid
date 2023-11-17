package com.bossondesign.christmascountdown_paid

import android.os.CountDownTimer
import java.util.Calendar

class ChristmasCountdownManager(private val listener: CountdownListener) {
    private var christmasDate: Long = 0
    private lateinit var timer: CountDownTimer

    interface CountdownListener {
        fun onCountdownUpdate(remainingDays: Int)
        fun onChristmasEve()
        fun onChristmasDay()
    }

    fun startCountdown() {
        val currentTime = System.currentTimeMillis()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        christmasDate = calculateChristmasDate(currentYear)

        if (currentTime >= christmasDate) {
            resetTimer()  // Set the date to the next year's Christmas
        }

        val millisUntilFinished = christmasDate - currentTime
        timer = object : CountDownTimer(millisUntilFinished, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingDays = (millisUntilFinished / (1000 * 60 * 60 * 24)).toInt()
                when (remainingDays) {
                    0 -> listener.onChristmasDay()
                    1 -> listener.onChristmasEve()
                    else -> listener.onCountdownUpdate(remainingDays)
                }
            }

            override fun onFinish() {
                if (Calendar.getInstance().timeInMillis >= christmasDate) {
                    resetTimer()
                }
                startCountdown()  // Restart the countdown
            }
        }
        timer.start()
    }

    private fun resetTimer() {
        val nextYear = Calendar.getInstance().get(Calendar.YEAR) + 1
        christmasDate = calculateChristmasDate(nextYear)
    }

    private fun calculateChristmasDate(year: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 25)
            set(Calendar.HOUR_OF_DAY, 23)
        }
        return calendar.timeInMillis
    }
}
