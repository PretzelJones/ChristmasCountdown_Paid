package com.bossondesign.christmascountdown_paid

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer

class ThemeMusicManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val sharedPrefs: SharedPreferences = context.getSharedPreferences("MusicPreference", Context.MODE_PRIVATE)
    private var resumePosition: Int = 0

    fun playMusic(musicName: String? = null) {
        val selectedMusicName = musicName ?: sharedPrefs.getString("selectedMusic", "merry_christmas")

        // Check if the selected music is "none"
        if (selectedMusicName == "none") {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            return
        }

        // Get the resource ID for the selected music
        val musicResId = selectedMusicName?.let { getMusicResId(it) }

        // Stop and release any currently playing music
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
        }
        mediaPlayer?.release()

        // Create and start playing the new music
        mediaPlayer = musicResId?.let {
            MediaPlayer.create(context, it).apply {
                isLooping = true
                start()
            }
        }
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

}
