package com.bossondesign.christmascountdown_paid

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.Calendar
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private val bellSoundMediaPlayers = LinkedList<MediaPlayer>()
    private val maxMediaPlayerInstances = 10

    private var countdownTextView: TextView? = null
    private var daystillTextView: TextView? = null
    private var textView: TextView? = null


    private var bellImage: ImageView? = null

    private var length = 0
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timer: CountDownTimer
    private var christmasDate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countdownTextView = findViewById(R.id.txtTimerDay)
        daystillTextView = findViewById(R.id.dayTillText)
        textView = findViewById(R.id.textPrivacy)
        bellImage = findViewById(R.id.imageView)

        showFeedbackDialog()

        // Calculate the initial halloweenDate based on the current year's Halloween
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        christmasDate = calculateChristmasDate(currentYear)

        // Reference the TextViews from your layout
        countdownTextView = findViewById(R.id.txtTimerDay)
        daystillTextView = findViewById(R.id.dayTillText)

        // Start the countdown timer
        startCountdown()

        textView?.movementMethod = LinkMovementMethod.getInstance()

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.merry_christmas)
        mediaPlayer?.start()
        mediaPlayer?.isLooping = true

        bellImage = findViewById(R.id.imageView)
        val myRotation = AnimationUtils.loadAnimation(applicationContext, R.anim.animation)

        bellImage?.setOnClickListener {
            bellImage?.startAnimation(myRotation)
            playBellSound()
        }

    }

    private fun resetTimer() {
        // Calculate the next year's Christmas
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val nextYear = currentYear + 1
        christmasDate = calculateChristmasDate(nextYear)

        startCountdown()
    }

    private fun startCountdown() {
        timer = object : CountDownTimer(christmasDate - System.currentTimeMillis(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer()
            }
        }
        timer.start()
    }

    private fun updateCountdown(timeRemaining: Long) {
        when (val remainingDays = (timeRemaining / (1000 * 60 * 60 * 24)).toInt()) {
            0 -> {
                daystillTextView?.text = getString(R.string.christmas)
                countdownTextView?.visibility = View.INVISIBLE
            }
            1 -> {
                daystillTextView?.text = getString(R.string.christmasEve)
                countdownTextView?.visibility = View.INVISIBLE
            }
            else -> {
                daystillTextView?.text = getString(R.string.countdown)
                countdownTextView?.text = "$remainingDays"
                countdownTextView?.visibility = View.VISIBLE
            }
        }
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

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result)
            }
        }
    }

    private fun playBellSound() {
        // Check and release the oldest MediaPlayer if limit is reached
        if (bellSoundMediaPlayers.size >= maxMediaPlayerInstances) {
            bellSoundMediaPlayers.poll()?.release()
        }

        // Create a new MediaPlayer for the bell sound
        MediaPlayer.create(this, R.raw.bell)?.also { mediaPlayer ->
            bellSoundMediaPlayers.offer(mediaPlayer)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
                bellSoundMediaPlayers.remove(mp)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        // Release bell MediaPlayer instances when the activity is destroyed
        while (!bellSoundMediaPlayers.isEmpty()) {
            bellSoundMediaPlayers.poll()?.release()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.apply {
            pause()
            length = currentPosition
        }
    }

    override fun onRestart() {
        super.onRestart()
        mediaPlayer?.apply {
            start()
            seekTo(length)
        }
    }
}
