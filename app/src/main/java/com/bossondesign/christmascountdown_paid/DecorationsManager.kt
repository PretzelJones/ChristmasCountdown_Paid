package com.bossondesign.christmascountdown_paid

import android.content.Context
import android.media.MediaPlayer
import android.widget.ImageView
import java.util.LinkedList

class DecorationManager(private val context: Context,
                        private val imageView: ImageView,
                        private val containerWidth: Int,   // Added to hold container width
                        private val containerHeight: Int) { // Added to hold container height

    //private var mediaPlayer: MediaPlayer? = null
    private var currentSoundResId: Int = 0 // Store the current sound resource ID
    private var currentDecoration: Decoration? = null
    private val soundMediaPlayers = LinkedList<MediaPlayer>()
    private val maxMediaPlayerInstances = 10

    fun setDefaultDecoration() {
        val prefs = context.getSharedPreferences("DecorationPrefs", Context.MODE_PRIVATE)
        val decorationName = prefs.getString("lastDecoration", Decoration.JINGLE_BELLS.name)
        val decoration = Decoration.valueOf(decorationName ?: Decoration.JINGLE_BELLS.name)
        changeDecoration(decoration) // Set the default decoration
    }
    fun changeDecoration(decoration: Decoration) {
        imageView.setImageResource(decoration.imageResId)
        currentDecoration = decoration
        currentSoundResId = decoration.soundResId

        // Reset the imageView's properties
        imageView.scaleX = 1f
        imageView.scaleY = 1f
        imageView.translationX = 0f
        imageView.translationY = 0f

        saveDecorationPreference(decoration)
    }

    fun playCurrentAnimation() {
        currentDecoration?.let { playAnimation(it) }
    }

    fun playCurrentSound() {
        playSound(currentSoundResId)
    }

    private fun playAnimation(decoration: Decoration) {
        when (decoration) {
            Decoration.COCOA_MUG -> Animations.mugAnimation(imageView).start()
            Decoration.JINGLE_BELLS -> Animations.bellAnimation(imageView).start()
            Decoration.CHRISTMAS_TREE -> Animations.treeShakeAnimation2(imageView).start()
            Decoration.SLEIGH_BELLS -> Animations.sleighAnimation(imageView).start()
            Decoration.SNOWMAN -> Animations.swayAnimation(imageView).start()
            Decoration.NORTH_STAR -> Animations.growAndSpinAnimation(imageView).start()
        }
    }

    private fun saveDecorationPreference(decoration: Decoration) {
        val prefs = context.getSharedPreferences("DecorationPrefs", Context.MODE_PRIVATE)
        prefs.edit().putString("lastDecoration", decoration.name).apply()
    }

    private fun playSound(soundResId: Int) {
        val volume = currentDecoration?.volume ?: 0.3f // Default volume if not specified

        if (soundMediaPlayers.size >= maxMediaPlayerInstances) {
            soundMediaPlayers.poll()?.release()
        }

        MediaPlayer.create(context, soundResId)?.also { mediaPlayer ->
            soundMediaPlayers.offer(mediaPlayer)

            mediaPlayer.setVolume(volume, volume) // Set individual volume for each sound
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
                soundMediaPlayers.remove(mp)
            }
        }
    }

    fun release() {
        while (!soundMediaPlayers.isEmpty()) {
            soundMediaPlayers.poll()?.release()
        }
    }
}

enum class Decoration(val imageResId: Int, val soundResId: Int, val volume: Float) {
    JINGLE_BELLS(R.drawable.bells, R.raw.bells_ring, 0.1f),
    CHRISTMAS_TREE(R.drawable.christmas_tree, R.raw.tree_shake, 0.5f),
    COCOA_MUG(R.drawable.cocoa_mug, R.raw.cocoa_plop, 0.5f),
    SLEIGH_BELLS(R.drawable.sleigh_bells, R.raw.sleigh_bells_short, 1.0f),
    SNOWMAN(R.drawable.snowman, R.raw.snowman_magic, 0.5f),
    NORTH_STAR(R.drawable.north_star, R.raw.chime, 0.5f)
    // Add more decorations here
}
