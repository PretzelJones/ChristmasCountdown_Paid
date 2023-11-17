package com.bossondesign.christmascountdown_paid

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.google.android.play.core.review.ReviewManagerFactory
import java.util.LinkedList

class MainActivity : AppCompatActivity(), ChristmasCountdownManager.CountdownListener {

    private val bellSoundMediaPlayers = LinkedList<MediaPlayer>()
    private val maxMediaPlayerInstances = 10

    private var countdownTextView: TextView? = null
    private var daystillTextView: TextView? = null
    private var textView: TextView? = null
    private var bellImage: ImageView? = null

    private lateinit var themeMusicManager: ThemeMusicManager
    private lateinit var wallpaperManager: WallpaperManager

    //private var length = 0
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

        themeMusicManager = ThemeMusicManager(this)
        themeMusicManager.playMusic()

        // Initialize WallpaperManager with the layout ID
        wallpaperManager = WallpaperManager(this, R.id.main_layout)
        wallpaperManager.loadWallpaperPreference()

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Make both the navigation bar and the status bar transparent
        window.statusBarColor = Color.parseColor("#66000000")
        window.navigationBarColor = Color.TRANSPARENT

        val myRotation = AnimationUtils.loadAnimation(applicationContext, R.anim.animation)
        bellImage?.setOnClickListener {
            bellImage?.startAnimation(myRotation)
            playBellSound()
        }

        val settings = findViewById<ImageView>(R.id.settingsIcon)
        settings.setOnClickListener { view ->
            showMenu(view)
        }
    }

    private fun showMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.settings_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.music_merry_christmas -> {
                    themeMusicManager.playMusic("merry_christmas")
                    themeMusicManager.saveMusicPreference("merry_christmas")
                    true
                }
                R.id.music_christmas_tree -> {
                    themeMusicManager.playMusic("christmas_tree")
                    themeMusicManager.saveMusicPreference("christmas_tree")
                    true
                }
                R.id.music_deck_halls -> {
                    themeMusicManager.playMusic("deck_halls")
                    themeMusicManager.saveMusicPreference("deck_halls")
                    true
                }
                R.id.music_jingle_bells -> {
                    themeMusicManager.playMusic("jingle_bells")
                    themeMusicManager.saveMusicPreference("jingle_bells")
                    true
                }
                R.id.music_silent_night -> {
                    themeMusicManager.playMusic("silent_night")
                    themeMusicManager.saveMusicPreference("silent_night")
                    true
                }
                R.id.music_holy_night -> {
                    themeMusicManager.playMusic("holy_night")
                    themeMusicManager.saveMusicPreference("holy_night")
                    true
                }
                R.id.music_none -> {
                    themeMusicManager.stopMusicAndSavePreference()
                    true
                }
                R.id.backgrounds -> {
                    // Your existing code for backgrounds selection
                    true
                }
                R.id.red_wall -> {
                    wallpaperManager.changeWallpaper("red_wall")
                    true
                }
                R.id.green_wall -> {
                    wallpaperManager.changeWallpaper("green_wall")
                    true
                }
                R.id.blue_wall -> {
                    wallpaperManager.changeWallpaper("blue_wall")
                    true
                }
                R.id.peach_wall -> {
                    wallpaperManager.changeWallpaper("peach_wall")
                    true
                }
                R.id.black_wall -> {
                    wallpaperManager.changeWallpaper("black_wall")
                    true
                }
                R.id.grey_wall -> {
                    wallpaperManager.changeWallpaper("grey_wall")
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    /*
    private fun showMusicSelectionDialog() {
        val musicChoices = arrayOf(
            "christmas_tree",
            "deck_halls",
            "holy_night",
            "jingle_bells",
            "merry_christmas",
            "silent_night"
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Theme Music")
        builder.setItems(musicChoices) { _, which ->
            val selectedMusic = musicChoices[which]
            themeMusicManager.playMusic(selectedMusic)
            themeMusicManager.saveMusicPreference(selectedMusic)
        }
        builder.create().show()
    }

    private fun showBackgroundSelection() {
        // Implement a dialog to let the user choose the music
        // On selection, call themeMusicManager.playMusic(selectedMusicName)
        // and themeMusicManager.saveMusicPreference(selectedMusicName)
    }
*/
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

            mediaPlayer.setVolume(0.3f, 0.3f)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
                bellSoundMediaPlayers.remove(mp)
            }
        }
    }

    // New lifecycle methods for bell sounds management
    private fun pauseBellSounds() {
        bellSoundMediaPlayers.forEach { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }
    }

    private fun stopBellSounds() {
        bellSoundMediaPlayers.forEach { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.prepare() // Call prepare after stop
            }
        }
    }

    private fun releaseBellSoundMediaPlayers() {
        while (!bellSoundMediaPlayers.isEmpty()) {
            bellSoundMediaPlayers.poll()?.release()
        }
    }

    override fun onPause() {
        super.onPause()
        themeMusicManager.pauseMusic() // Pause the theme music
        pauseBellSounds() // Pause the bell sounds
    }

    override fun onResume() {
        super.onResume()
        themeMusicManager.resumeMusic() // Resume the theme music
        // Resume bell sounds if needed
    }

    override fun onStop() {
        super.onStop()
        // Stop bell sounds if they should not play in the background
        stopBellSounds()
    }

    override fun onDestroy() {
        super.onDestroy()
        themeMusicManager.releaseMediaPlayer() // Release the theme music player
        releaseBellSoundMediaPlayers() // Release the bell sound players
    }
}
