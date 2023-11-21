package com.bossondesign.christmascountdown_paid

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.PowerManager

class ThemeMusicManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("MusicPreference", Context.MODE_PRIVATE)
    private var resumePosition: Int = 0
    //private var currentVolume: Float = 1.0f // Default volume
    private var wakeLock: PowerManager.WakeLock? = null

    init {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MusicWakeLock")
    }

    fun playMusic(musicName: String? = null, volume: Float = 0.3f) {
        val selectedMusicName = musicName ?: sharedPrefs.getString("selectedMusic", "merry_christmas") ?: "default_music"

        if (selectedMusicName == "none") {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            releaseWakeLock() // Release wake lock if held
            return
        }

        val musicResId = getMusicResId(selectedMusicName)

        mediaPlayer?.stop()
        mediaPlayer?.release()
        releaseWakeLock() // Release any previously held wake lock

        mediaPlayer = MediaPlayer.create(context, musicResId).apply {
            isLooping = true
            setVolume(volume, volume)
            start()
        }

        acquireWakeLock() // Acquire a new wake lock
    }


    fun stopMusicAndSavePreference() {
        this.stopMusic()
        this.saveMusicPreference("none") // Using "none" to indicate no music
    }

    fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            resumePosition = mediaPlayer?.currentPosition ?: 0
            mediaPlayer?.pause()
        }
    }

    fun resumeMusic() {
        if (mediaPlayer == null) {
            playMusic() // Recreate the MediaPlayer if it was released
        } else {
            mediaPlayer?.seekTo(resumePosition)
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        releaseWakeLock() // Release wake lock when stopping music
        resumePosition = 0
    }


    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun getMusicResId(musicName: String): Int {
        return when (musicName) {
            "christmas_tree" -> R.raw.christmas_tree
            "deck_halls" -> R.raw.deck_halls
            "holy_night" -> R.raw.holy_night
            "jingle_bells" -> R.raw.jingle_bells
            "merry_christmas" -> R.raw.merry_christmas
            "silent_night" -> R.raw.silent_night
            "holly_jolly" -> R.raw.holly_jolly
            "frosty_snowman" -> R.raw.frosty_snowman
            else -> R.raw.merry_christmas
        }
    }

    fun saveMusicPreference(musicName: String) {
        sharedPrefs.edit().putString("selectedMusic", musicName).apply()
        if (musicName != "none") {
            playMusic(musicName)
        } else {
            stopMusic() // Stop music if "none" is selected
        }
    }

    private fun acquireWakeLock() {
        if (wakeLock?.isHeld == false) {
            wakeLock?.acquire(10*60*1000L /*10 minutes*/)
        }
    }

    private fun releaseWakeLock() {
        if (wakeLock?.isHeld == true) {
            wakeLock?.release()
        }
    }
}
