package com.bossondesign.christmascountdown_paid

import android.media.MediaPlayer
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.LinkedList

class MainActivity : AppCompatActivity(), ChristmasCountdownManager.CountdownListener {
    private var mediaPlayer: MediaPlayer? = null
    private val bellSoundMediaPlayers = LinkedList<MediaPlayer>()
    private val maxMediaPlayerInstances = 10

    private var countdownTextView: TextView? = null
    private var daystillTextView: TextView? = null
    private var textView: TextView? = null
    private var bellImage: ImageView? = null

    private var length = 0
    private lateinit var countdownManager: ChristmasCountdownManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countdownTextView = findViewById(R.id.txtTimerDay)
        daystillTextView = findViewById(R.id.dayTillText)
        textView = findViewById(R.id.textPrivacy)
        bellImage = findViewById(R.id.imageView)

        showFeedbackDialog()

        countdownManager = ChristmasCountdownManager(this)
        countdownManager.startCountdown()

        textView?.movementMethod = LinkMovementMethod.getInstance()

        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.merry_christmas)
        mediaPlayer?.start()
        mediaPlayer?.isLooping = true

        val myRotation = AnimationUtils.loadAnimation(applicationContext, R.anim.animation)
        bellImage?.setOnClickListener {
            bellImage?.startAnimation(myRotation)
            playBellSound()
        }
    }

    override fun onCountdownUpdate(remainingDays: Int) {
        daystillTextView?.text = getString(R.string.countdown)
        countdownTextView?.text = "$remainingDays"
        countdownTextView?.visibility = View.VISIBLE
    }

    override fun onChristmasEve() {
        daystillTextView?.text = getString(R.string.christmasEve)
        countdownTextView?.visibility = View.INVISIBLE
    }

    override fun onChristmasDay() {
        daystillTextView?.text = getString(R.string.christmas)
        countdownTextView?.visibility = View.INVISIBLE
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
        if (bellSoundMediaPlayers.size >= maxMediaPlayerInstances) {
            bellSoundMediaPlayers.poll()?.release()
        }

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
        while (!bellSoundMediaPlayers.isEmpty()) {
            bellSoundMediaPlayers.poll()?.release()
        }
        mediaPlayer?.release()
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
